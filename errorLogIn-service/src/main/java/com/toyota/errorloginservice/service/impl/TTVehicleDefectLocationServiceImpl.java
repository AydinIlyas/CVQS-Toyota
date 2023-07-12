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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service class for managing tt_vehicle_defect_location data.
 */
@Service
@RequiredArgsConstructor
public class TTVehicleDefectLocationServiceImpl implements TTVehicleDefectLocationService {
    private final TTVehicleDefectLocationRepository locationRepository;
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
        logger.info("Adding location to defect. Defect ID: {}",defectId);
        Optional<TTVehicleDefect> optionalDefect = defectRepository.findById(defectId);

        if (optionalDefect.isPresent()) {

            TTVehicleDefect defect = optionalDefect.get();
            if(defect.getDefectImage()==null)
            {
                logger.warn("Defect has no image!");
                throw new ImageNotFoundException("Defect has no image!");
            }
            isLocationValid(defect,defectLocationDTO);
            String color=isValidColorHex(defectLocationDTO.getColorHex())? defectLocationDTO.getColorHex() : "#FF0000";
            TTVehicleDefectLocation location = TTVehicleDefectLocation.builder()
                    .x_Axis(defectLocationDTO.getX_Axis())
                    .y_Axis(defectLocationDTO.getY_Axis())
                    .height(defectLocationDTO.getHeight())
                    .width(defectLocationDTO.getWidth())
                    .colorHex(color)
                    .build();
            if(defect.getLocation()==null)
                defect.setLocation(new ArrayList<>());
            defect.getLocation().add(location);
            location.setTt_vehicle_defect(defect);
            TTVehicleDefectLocation saved= locationRepository.save(location);
            logger.info("Successfully added location to defect. Location ID: {}, Defect ID: {}",
                    saved.getId(),defectId);
        } else {
            logger.warn("Defect not found! ID:{}",defectId);
            throw new EntityNotFoundException("Defect not found! ID: " + defectId);
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
        logger.info("Deleting location. ID: {}",locationId);
        Optional<TTVehicleDefectLocation> optionalLocation = locationRepository.findById(locationId);
        if (optionalLocation.isPresent()) {
            TTVehicleDefectLocation location = optionalLocation.get();
            location.setDeleted(true);
            locationRepository.save(location);
            logger.info("Deleted location successfully! Location ID: {}, Defect ID:{}",
                    location.getId(),location.getTt_vehicle_defect().getId());
        }
        else{
            logger.warn("Location not found! Location ID: {}",locationId);
            throw new EntityNotFoundException("Location not found! Location ID: "+locationId);
        }
    }

    /**
     * @param id ID of Location
     * @param locationDTO Updated LocationDTO
     */
    @Override
    public void update(Long id, TTVehicleDefectLocationDTO locationDTO) {
        logger.info("Updating Location. ID: {}",id);
        Optional<TTVehicleDefectLocation> optionalLocation= locationRepository.findById(id);
        if(optionalLocation.isPresent())
        {
            TTVehicleDefectLocation location=optionalLocation.get();
            isLocationValid(location.getTt_vehicle_defect(),locationDTO);
            if(locationDTO.getX_Axis()!=null&&locationDTO.getX_Axis()!=location.getX_Axis())
            {
                location.setX_Axis(locationDTO.getX_Axis());
                logger.debug("Location x-axis updated: {}",location.getX_Axis());
            }
            if(locationDTO.getY_Axis()!=null&&locationDTO.getY_Axis()!=location.getY_Axis())
            {
                location.setY_Axis(locationDTO.getY_Axis());
                logger.debug("Location y-axis updated: {}",location.getX_Axis());
            }
            if(locationDTO.getWidth()!=null&&locationDTO.getWidth()!=location.getWidth())
            {
                location.setWidth(locationDTO.getWidth());
                logger.debug("Location width updated: {}",location.getWidth());
            }
            if(locationDTO.getHeight()!=null&&locationDTO.getHeight()!=location.getHeight())
            {
                location.setHeight(locationDTO.getHeight());
                logger.debug("Location height updated: {}",location.getHeight());
            }
            if(locationDTO.getColorHex()!=null&&isValidColorHex(locationDTO.getColorHex()))
            {
                location.setColorHex(locationDTO.getColorHex());
                logger.debug("Location color updated: {}",location.getColorHex());
            }
            locationRepository.save(location);
            logger.info("Updated location successfully. Location ID: {}",id);
        }
        else{
            logger.warn("Location not found. Location ID: {}",id);
            throw new EntityNotFoundException("Location not found! ID: "+id);
        }

    }
    private boolean isValidColorHex(String color) {
        if(color==null)return false;
        String regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(color);
        return matcher.matches();

    }
    private void isLocationValid(TTVehicleDefect defect, TTVehicleDefectLocationDTO defectLocationDTO)
    {
        try
        {
            BufferedImage bufferedImage= ImageIO.read(new ByteArrayInputStream(defect.getDefectImage()));
            if(bufferedImage.getHeight()<defectLocationDTO.getY_Axis())
            {
                logger.warn("Y-Axis of location is exceeding maximum width of image. Max: {}",
                        bufferedImage.getWidth());
                throw new InvalidLocationException("Y-Axis of location is exceeding maximum height of image. Max: "
                        +bufferedImage.getHeight());
            }
            if(bufferedImage.getWidth()<defectLocationDTO.getX_Axis())
            {
                logger.warn("X-Axis of location is exceeding maximum width of image. Max: {}",
                        bufferedImage.getWidth());
                throw new InvalidLocationException("X-Axis of location is exceeding maximum width of image. Max: "
                        +bufferedImage.getWidth());
            }
        }
        catch(IOException e){
            logger.warn("IOException while reading image. {}",e.getMessage());
            throw new ImageProcessingException("Image reading failed! "+e.getMessage());
        }
    }
}
