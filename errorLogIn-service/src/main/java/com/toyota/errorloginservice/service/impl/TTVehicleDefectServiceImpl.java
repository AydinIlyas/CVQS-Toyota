package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleDefectRepository;
import com.toyota.errorloginservice.dao.TTVehicleRepository;
import com.toyota.errorloginservice.domain.TTVehicle;
import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import com.toyota.errorloginservice.dto.ImageDTO;
import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorloginservice.exception.EntityNotFoundException;
import com.toyota.errorloginservice.exception.ImageNotFoundException;
import com.toyota.errorloginservice.exception.ImageProcessingException;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectService;
import com.toyota.errorloginservice.service.common.MapUtil;
import com.toyota.errorloginservice.service.common.SortUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing tt_vehicle_defect data.
 */
@Service
@RequiredArgsConstructor
public class TTVehicleDefectServiceImpl implements TTVehicleDefectService {
    private final TTVehicleDefectRepository ttVehicleDefectRepository;
    private final TTVehicleRepository ttVehicleRepository;
    private final MapUtil mapUtil;
    private final Logger logger = LogManager.getLogger(TTVehicleDefectServiceImpl.class);


    /**
     * Gets defects with paging, filtering and sorting
     * @param page desired page
     * @param size size of page
     * @param type       desired type of defect
     * @param state      desired state of defect
     * @param reportTime desired reportTime of defect
     * @param reportedBy desired reporter of defect
     * @param vin        desired vin of defect
     * @param sortOrder  desired sort direction
     * @param sortBy     desired fields for sorting
     * @return PaginationResponse with a list of TTVehicleDefectDTO
     */
    @Override
    public PaginationResponse<TTVehicleDefectDTO> getAllFiltered(int page, int size, String type, String state, String reportTime,
                                                                 String reportedBy, String vin, String sortOrder,
                                                                 List<String> sortBy) {
        logger.info("Fetching vehicles.");
        Pageable pageable = PageRequest.of(page, size, Sort.by(SortUtil.createSortOrder(sortBy, sortOrder)));
        Page<TTVehicleDefect> pageResponse = ttVehicleDefectRepository
                .getDefectsFiltered(type, state, reportTime, reportedBy, vin, pageable);
        logger.debug("Retrieved {} defects.", pageResponse.getContent().size());
        List<TTVehicleDefectDTO> ttVehicleDefectDTOS = pageResponse.stream().map(
                mapUtil::convertDefectToDTO
        ).collect(Collectors.toList());
        logger.info("Retrieved and converted {} vehicles to dto.", ttVehicleDefectDTOS.size());
        return new PaginationResponse<>(ttVehicleDefectDTOS, pageResponse);
    }

    /**
     * Adds image to defect
     * @param defectId ID of the defect.
     * @param image    Image to add.
     */
    @Override
    public void addImage(Long defectId, MultipartFile image) {
        Optional<TTVehicleDefect> optionalDefect = ttVehicleDefectRepository.findById(defectId);
        if (optionalDefect.isPresent()) {
            TTVehicleDefect defect = optionalDefect.get();
            defect.getLocation().forEach(l -> l.setDeleted(true));
            defect.setLocation(new ArrayList<>());
            try {
                byte[] bytes = image.getBytes();
                if (bytes.length == 0) {
                    logger.warn("Failed to add image. Image not found.");
                    throw new ImageNotFoundException("Failed to add image. Image not found.");
                }
                defect.setDefectImage(bytes);
                ttVehicleDefectRepository.save(defect);
            } catch (IOException e) {
                logger.warn("Failed to get bytes of image.");
                throw new ImageProcessingException("Failed to get bytes of image.");
            }
        } else {
            logger.warn("Failed to add image. Defect not Found!");
            throw new EntityNotFoundException("Failed to add image. Defect Not Found!");
        }
    }

    /**
     * Gets image with associated locations
     * @param defectId ID of the defect.
     * @return ImageDTO which contains image and locations
     */
    @Override
    public ImageDTO getImage(Long defectId) {
        Optional<TTVehicleDefect> optionalDefect = ttVehicleDefectRepository.findById(defectId);
        if (optionalDefect.isPresent()) {
            TTVehicleDefect defect = optionalDefect.get();
            if (defect.getDefectImage() == null)
            {
                logger.warn("Defect has no image.");
                return null;
            }
            List<TTVehicleDefectLocationDTO> list = defect.getLocation().stream().map(mapUtil::convertLocationDTO)
                    .collect(Collectors.toList());
            logger.info("Fetched Image and locations successfully.");
            return new ImageDTO(defect.getDefectImage(), list);
        }
        logger.warn("Failed to fetch image. Defect has no Image.");
        throw new EntityNotFoundException("Failed to fetch image.Defect Not Found");
    }

    /**
     * Removes image from defect
     * @param defectId ID of defect
     */
    @Override
    public void removeImage(Long defectId) {
        Optional<TTVehicleDefect> optionalDefect = ttVehicleDefectRepository.findById(defectId);
        if (optionalDefect.isPresent())
        {
            TTVehicleDefect defect=optionalDefect.get();
            defect.setDefectImage(null);
            ttVehicleDefectRepository.save(defect);
        }
        else{
            logger.warn("Failed to remove image. Defect Not Found.");
            throw new EntityNotFoundException("Failed to remove image. Defect Not Found.");
        }
    }

    /**
     * Adds a defect to vehicle.
     * @param vehicleId ID of the vehicle.
     * @param defectDTO TTVehicleDefectDTO which will be added to the vehicle.
     */
    @Override
    public TTVehicleDefectDTO addDefect(String username, Long vehicleId,
                                        TTVehicleDefectDTO defectDTO) {
        logger.info("Adding defect to vehicle. Vehicle ID: {}, Defect Type: {}", vehicleId, defectDTO.getType());
        Optional<TTVehicle> optionalTTVehicle = ttVehicleRepository.findById(vehicleId);
        if (optionalTTVehicle.isPresent()) {
            TTVehicleDefect defect = mapUtil.convertDefectDTOToEntity(defectDTO);
            defect.setReportedBy(username);
            defect.setReportTime(LocalDateTime.now());
            TTVehicle ttVehicle = optionalTTVehicle.get();
            ttVehicle.getDefect().add(defect);
            defect.setTt_vehicle(ttVehicle);
            TTVehicleDefect saved = ttVehicleDefectRepository.save(defect);
            logger.info("Defect created successfully! Vehicle ID: {}, Defect ID: {}", vehicleId, defect.getId());
            return mapUtil.convertDefectToDTO(saved);
        } else {
            logger.warn("Vehicle not found! Vehicle ID: {}", vehicleId);
            throw new EntityNotFoundException("Vehicle not found! Vehicle ID: " + vehicleId);
        }

    }

    /**
     * Updates defect
     *
     * @param id ID of defect
     * @param defectDTO Updated defect
     * @return TTVehicleDefectDTO with the updated defect
     */
    @Override
    public TTVehicleDefectDTO update(Long id, TTVehicleDefectDTO defectDTO) {
        logger.info("Updating defect with ID: {}", id);
        Optional<TTVehicleDefect> optionalDefect = ttVehicleDefectRepository.findById(id);
        if (optionalDefect.isPresent()) {
            TTVehicleDefect defect = optionalDefect.get();
            if (defectDTO.getType() != null && !defect.getType().equals(defectDTO.getType())) {
                defect.setType(defectDTO.getType());
                logger.debug("Defect type updated: {}", defect.getType());
            }

            if (defectDTO.getDescription() != null && !defect.getDescription().equals(defectDTO.getDescription())) {
                defect.setDescription(defectDTO.getDescription());
                logger.debug("Defect description updated: {}", defect.getDescription());
            }
            if (defectDTO.getState() != null && !defect.getState().equals(defectDTO.getState())) {
                defect.setState(defectDTO.getState());
                logger.debug("Defect state updated: {}", defect.getState());
            }
            ttVehicleDefectRepository.save(defect);
            logger.info("Defect updated successfully! Defect ID: {}", id);
            return mapUtil.convertDefectToDTO(defect);
        }
        logger.warn("Defect not found! Defect ID: {}", id);
        throw new EntityNotFoundException("Defect not found! DEFECT ID: " + id);
    }


    /**
     * Soft deletes defect and associated locations, if present.
     *
     * @param defectId Defect id of the defect which will be deleted.
     * @return TTVehicleDefectDTO which was deleted
     */
    @Override
    @Transactional
    public TTVehicleDefectDTO deleteDefect(Long defectId) {
        logger.info("Deleting defect with ID: {}", defectId);
        Optional<TTVehicleDefect> optionalDefect = ttVehicleDefectRepository.findById(defectId);
        if (optionalDefect.isPresent()) {
            TTVehicleDefect defect = optionalDefect.get();
            List<TTVehicleDefectLocation> locations = defect.getLocation();
            locations.forEach(l -> {
                l.setDeleted(true);
                logger.info("Deleted with defect associated locations. Location ID: {}", l.getId());
            });
            defect.setDeleted(true);
            TTVehicleDefect saved = ttVehicleDefectRepository.save(defect);
            logger.info("Deleted defect and all associated locations  successfully! Defect ID: {}", defectId);
            return mapUtil.convertDefectToDTO(saved);
        } else {
            logger.warn("Defect not found! ID: {}", defectId);
            throw new EntityNotFoundException("Defect not found! ID: " + defectId);
        }
    }


}
