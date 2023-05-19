package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleDefectRepository;
import com.toyota.errorloginservice.dao.TTVehicleRepository;
import com.toyota.errorloginservice.domain.TTVehicle;
import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.exception.EntityNotFoundException;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TTVehicleDefectServiceImpl implements TTVehicleDefectService {
    private final TTVehicleDefectRepository ttVehicleDefectRepository;
    private final TTVehicleRepository ttVehicleRepository;
    private Logger logger= LogManager.getLogger(TTVehicleDefectServiceImpl.class);

    /**
     * @param vehicleId
     */
    @Override
    public void addDefect(Long vehicleId, TTVehicleDefectDTO defectDTO) {
        Optional<TTVehicle> optionalTTVehicle = ttVehicleRepository.findById(vehicleId);
        if (optionalTTVehicle.isPresent()) {
            TTVehicleDefect defect = TTVehicleDefect.builder()
                    .type(defectDTO.getType())
                    .defectImage(defectDTO.getDefectImage())
                    .location(defectDTO.getLocation())
                    .build();
            TTVehicle ttVehicle = optionalTTVehicle.get();
            ttVehicle.getDefect().add(defect);
            defect.setTt_vehicle(ttVehicle);
            TTVehicle saved = ttVehicleRepository.save(ttVehicle);
            logger.info("Created defect successfully");
        }
        else{
            logger.warn("Vehicle with id {} couldn't found!",vehicleId);
            throw new EntityNotFoundException("There is no Vehicle with the id: "+vehicleId);
        }

    }

    /**
     * @param defectId
     */
    @Override
    @Transactional
    public void deleteDefect(Long defectId) {
        Optional<TTVehicleDefect> optionalDefect = ttVehicleDefectRepository.findById(defectId);
        if (optionalDefect.isPresent()) {
            TTVehicleDefect defect = optionalDefect.get();
            List<TTVehicleDefectLocation> locations = defect.getLocation();
            locations.forEach(l -> l.setDeleted(true));
            defect.setDeleted(true);
            logger.info("Soft deleted defect successfully!");
        }
        else{
            logger.warn("Defect couldn't found! Id: {}",defectId);
            throw new EntityNotFoundException("Defect with id "+defectId+"could'nt found!");
        };
    }

}
