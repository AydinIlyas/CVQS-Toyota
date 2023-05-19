package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleDefectLocationRepository;
import com.toyota.errorloginservice.dao.TTVehicleDefectRepository;
import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorloginservice.exception.EntityNotFoundException;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectLocationService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TTVehicleDefectLocationServiceImpl implements TTVehicleDefectLocationService {
    private final TTVehicleDefectLocationRepository defectLocationRepository;
    private final TTVehicleDefectRepository defectRepository;
    private final Logger logger= LogManager.getLogger(TTVehicleDefectLocationServiceImpl.class);


    /**
     * @param defectId
     * @param defectLocationDTO
     * @return
     */
    @Override
    public void add(Long defectId, TTVehicleDefectLocationDTO defectLocationDTO) {
        Optional<TTVehicleDefect> optionalDefect = defectRepository.findById(defectId);

        if (optionalDefect.isPresent()) {
            TTVehicleDefectLocation location = TTVehicleDefectLocation.builder()
                    .x_Axis(defectLocationDTO.getX_Axis())
                    .y_Axis(defectLocationDTO.getY_Axis())
                    .build();
            TTVehicleDefect defect = optionalDefect.get();
            defect.getLocation().add(location);
            location.setTt_vehicle_defect(defect);
            TTVehicleDefect saved = defectRepository.save(defect);
            logger.info("Successfully added Location to defect with id {}",defectId);
        } else {
            logger.warn("There is no defect with id: {}",defectId);
            throw new EntityNotFoundException("There is no defect with id: " + defectId);
        }

    }

    /**
     * @param locationId
     * @return
     */
    @Override
    @Transactional
    public void delete(Long locationId) {
        Optional<TTVehicleDefectLocation> optionalLocation = defectLocationRepository.findById(locationId);
        if (optionalLocation.isPresent()) {
            TTVehicleDefectLocation location = optionalLocation.get();
            location.setDeleted(true);
            logger.info("Deleted location successfully from defect with id: {}",location.getTt_vehicle_defect().getId());
        }
        else{
            logger.warn("Delete failed! Location couldn't found! Id: {}",locationId);
            throw new EntityNotFoundException("Location with id "+locationId+"couldn't be found");
        }
    }
}
