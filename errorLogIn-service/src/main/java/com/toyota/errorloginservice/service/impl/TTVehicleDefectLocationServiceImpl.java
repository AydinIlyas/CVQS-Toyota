package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleDefectLocationRepository;
import com.toyota.errorloginservice.dao.TTVehicleDefectRepository;
import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TTVehicleDefectLocationServiceImpl implements TTVehicleDefectLocationService {
    private final TTVehicleDefectLocationRepository defectLocationRepository;
    private final TTVehicleDefectRepository defectRepository;


    /**
     * @param defectId
     * @param defectLocationDTO
     * @return
     */
    @Override
    public boolean add(Long defectId, TTVehicleDefectLocationDTO defectLocationDTO) {
        Optional<TTVehicleDefect> optionalDefect=defectRepository.findById(defectId);

        if(optionalDefect.isEmpty())
        {
            return false;
        }
        TTVehicleDefectLocation location=TTVehicleDefectLocation.builder()
                .x_Axis(defectLocationDTO.getX_Axis())
                .y_Axis(defectLocationDTO.getY_Axis())
                .build();
        TTVehicleDefect defect=optionalDefect.get();
        defect.getLocation().add(location);
        location.setTt_vehicle_defect(defect);
        TTVehicleDefect saved= defectRepository.save(defect);
        if (saved==null)
        {
            return false;
        }
        return true;

    }

    /**
     * @param locationId
     * @return
     */
    @Override
    @Transactional
    public boolean delete(Long locationId){
        Optional<TTVehicleDefectLocation> optionalLocation=defectLocationRepository.findById(locationId);
        if(optionalLocation.isEmpty())
        {
            return false;
        }
        TTVehicleDefectLocation location=optionalLocation.get();
        location.setDeleted(true);
        return true;
    }
}
