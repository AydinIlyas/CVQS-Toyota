package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleDefectLocationRepository;
import com.toyota.errorloginservice.dao.TTVehicleDefectRepository;
import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorloginservice.exception.EntityNotFoundException;
import com.toyota.errorloginservice.exception.ImageNotFoundException;
import com.toyota.errorloginservice.exception.ImageProcessingException;
import com.toyota.errorloginservice.exception.InvalidLocationException;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectLocationService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Service class for managing tt_vehicle_defect_location data.
 */
@Service
@RequiredArgsConstructor
public class TTVehicleDefectLocationServiceImpl implements TTVehicleDefectLocationService {
    private final TTVehicleDefectLocationRepository defectLocationRepository;
    private final TTVehicleDefectRepository defectRepository;
    private final Logger logger= LogManager.getLogger(TTVehicleDefectLocationServiceImpl.class);


    /**
     * Adding location to defect. First it checks if defect exists. If not it throws an EntityNotFoundException.
     * Else it adds it to the defect and saves it to the database.
     * @param defectId Defect id where the location should be added.
     * @param defectLocationDTO Location object which will be added.
     */
    @Override
    public void add(Long defectId, TTVehicleDefectLocationDTO defectLocationDTO) {
        Optional<TTVehicleDefect> optionalDefect = defectRepository.findById(defectId);

        if (optionalDefect.isPresent()) {

            TTVehicleDefect defect = optionalDefect.get();
            if(defect.getDefectImage()==null)
            {
                throw new ImageNotFoundException("Defect has no image!");
            }
            try
            {
                BufferedImage bufferedImage= ImageIO.read(new ByteArrayInputStream(defect.getDefectImage()));
                if(bufferedImage.getHeight()<defectLocationDTO.getY_Axis())
                {
                    throw new InvalidLocationException("Y Axis of location is exceeding maximum height of image. Max: "
                            +bufferedImage.getHeight());
                }
                if(bufferedImage.getWidth()<defectLocationDTO.getX_Axis())
                {
                    throw new InvalidLocationException("X Axis of location is exceeding maximum width of image. Max: "
                            +bufferedImage.getWidth());
                }
            }
            catch(IOException e){
                throw new ImageProcessingException("Image reading failed!");
            }
            TTVehicleDefectLocation location = TTVehicleDefectLocation.builder()
                    .x_Axis(defectLocationDTO.getX_Axis())
                    .y_Axis(defectLocationDTO.getY_Axis())
                    .build();
            if(defect.getLocation()==null)
                defect.setLocation(new ArrayList<>());
            defect.getLocation().add(location);
            location.setTt_vehicle_defect(defect);
            defectLocationRepository.save(location);
            logger.info("Successfully added Location to defect with id {}",defectId);
        } else {
            logger.warn("There is no defect with id: {}",defectId);
            throw new EntityNotFoundException("There is no defect with id: " + defectId);
        }

    }

    /**
     * Soft deletes the location. First, it is checked whether a location exists.
     * Then it will be soft deleted if it exists.
     * @param locationId Location id of the entity which will be deleted.
     */
    @Override
    @Transactional
    public void delete(Long locationId) {
        Optional<TTVehicleDefectLocation> optionalLocation = defectLocationRepository.findById(locationId);
        if (optionalLocation.isPresent()) {
            TTVehicleDefectLocation location = optionalLocation.get();
            location.setDeleted(true);
            defectLocationRepository.save(location);
            logger.info("Deleted location successfully! LOCATION ID: {}, DEFECT ID:{}",
                    location.getId(),location.getTt_vehicle_defect().getId());
        }
        else{
            logger.warn("Delete failed! Location couldn't found! Id: {}",locationId);
            throw new EntityNotFoundException("Location with id "+locationId+"couldn't be found");
        }
    }
}
