package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleDefectRepository;
import com.toyota.errorloginservice.dao.TTVehicleRepository;
import com.toyota.errorloginservice.domain.TTVehicle;
import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectResponse;
import com.toyota.errorloginservice.exception.EntityNotFoundException;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing tt_vehicle_defect data.
 */
@Service
@RequiredArgsConstructor
public class TTVehicleDefectServiceImpl implements TTVehicleDefectService {
    private final TTVehicleDefectRepository ttVehicleDefectRepository;
    private final TTVehicleRepository ttVehicleRepository;
    private final ModelMapper modelMapper;
    private final Logger logger= LogManager.getLogger(TTVehicleDefectServiceImpl.class);


    /**
     * Adds a defect to the vehicle if present.
     * @param vehicleId Vehicle id of the vehicle which has the defect.
     * @param defectDTO Defect object which will added to the vehicle.
     */
    @Override
    public TTVehicleDefectResponse addDefect(HttpServletRequest request,Long vehicleId,
                                             TTVehicleDefectDTO defectDTO) {
        Optional<TTVehicle> optionalTTVehicle = ttVehicleRepository.findById(vehicleId);
        if (optionalTTVehicle.isPresent()) {
            TTVehicleDefect defect = convertToEntity(defectDTO);
            String username=(String)request.getAttribute("Username");
            defect.setReportedBy(username);
            TTVehicle ttVehicle = optionalTTVehicle.get();
            ttVehicle.getDefect().add(defect);
            defect.setTt_vehicle(ttVehicle);
            ttVehicleRepository.save(ttVehicle);
            logger.info("Created defect successfully");
            return convertToResponse(defect);
        }
        else{
            logger.warn("Vehicle with id {} couldn't found!",vehicleId);
            throw new EntityNotFoundException("There is no Vehicle with the id: "+vehicleId);
        }

    }

    /**
     * Soft deletes defect and associated locations, if present.
     * @param defectId Defect id of the defect which will be deleted.
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
            throw new EntityNotFoundException("Defect with id "+defectId+"couldn't found!");
        }
    }

    private TTVehicleDefect convertToEntity(TTVehicleDefectDTO defectDTO)
    {
        return modelMapper.map(defectDTO,TTVehicleDefect.class);
    }
    private TTVehicleDefectResponse convertToResponse(TTVehicleDefect defect)
    {
        return modelMapper.map(defect,TTVehicleDefectResponse.class);
    }

}
