package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleRepository;
import com.toyota.errorloginservice.domain.TTVehicle;
import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.exception.EntityNotFoundException;
import com.toyota.errorloginservice.exception.VehicleAlreadyExistsException;
import com.toyota.errorloginservice.service.abstracts.TTVehicleService;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing tt_vehicle related data.
 */
@Service
@RequiredArgsConstructor
public class TTVehicleServiceImpl implements TTVehicleService {
    private final TTVehicleRepository ttVehicleRepository;
    private final Logger logger = LogManager.getLogger(TTVehicleService.class);
    private final MapUtil mapUtil;


    /**
     * Gets vehicle with filtering, paging and sorting
     * @param page desired page
     * @param size size of the page
     * @param sortBy Sorted By field
     * @param sortOrder Sort Order (ASC/DESC)
     * @param model desired model
     * @param vin desired vin
     * @param yearOfProduction desired year of production
     * @param transmissionType desired transmission type
     * @param engineType desired engineType
     * @param color desired color
     * @return PaginationResponse with list of TTVehicleDTO
     */
    @Override
    public PaginationResponse<TTVehicleDTO> getVehiclesFiltered(int page,int size,List<String> sortBy, String sortOrder,
                                                  String model, String vin, String yearOfProduction,
                                                  String transmissionType, String engineType, String color) {
        logger.info("Fetching vehicles.");
        Pageable pageable= PageRequest.of(page,size,Sort.by(SortUtil.createSortOrder(sortBy,sortOrder)));
        Page<TTVehicle> pageResponse=ttVehicleRepository.getVehiclesFiltered(model,vin,yearOfProduction,
                transmissionType,engineType
        ,color,pageable);
        logger.debug("Retrieved {} vehicles.",pageResponse.getContent().size());
        List<TTVehicleDTO>ttVehicleDTOS=pageResponse.stream().map(
                mapUtil::convertVehicleWithAllToDTO
        ).collect(Collectors.toList());
        logger.info("Retrieved and converted {} vehicles to dto.",ttVehicleDTOS.size());
        return new PaginationResponse<>(ttVehicleDTOS,pageResponse);
    }


    /**
     * Adds TTVehicle to database.
     *
     * @param ttVehicleDTO Vehicle object which will be added to database.
     * @return TTVehicleDTO which represents the added vehicle.
     */
    @Override
    public TTVehicleDTO addVehicle(TTVehicleDTO ttVehicleDTO) {
        logger.info("Adding vehicle. VIN: {}",ttVehicleDTO.getVin());
        if(ttVehicleRepository.existsByVinAndDeletedIsFalse(ttVehicleDTO.getVin()))
        {
            logger.warn("Vehicle create failed due to VIN conflict: Vehicle with this VIN already exists!" +
                    " VIN: {}",ttVehicleDTO.getVin());
            throw new VehicleAlreadyExistsException("Vehicle with this vin already exists. Vin: "+ttVehicleDTO.getVin());
        }
        TTVehicle ttVehicle = mapUtil.convertVehicleDTOToEntity(ttVehicleDTO);
        TTVehicle saved = ttVehicleRepository.save(ttVehicle);
        logger.info("Vehicle added successfully! ID: {}, VIN: {}",saved.getId(),saved.getVin());
        return mapUtil.convertVehicleWithAllToDTO(saved);
    }

    /**
     * Updates Vehicle
     * @param id ID of vehicle
     * @param ttVehicleDTO Updated vehicle
     * @return TTVehicleDTO which represents updated vehicle
     */
    @Override
    public TTVehicleDTO updateVehicle(Long id,TTVehicleDTO ttVehicleDTO)
    {
        logger.info("Updating vehicle with ID: {}",id);
        Optional<TTVehicle> optionalTTVehicle=ttVehicleRepository.findById(id);
        if(optionalTTVehicle.isPresent())
        {
            TTVehicle vehicle=optionalTTVehicle.get();

            if(ttVehicleDTO.getVin()!=null&&!vehicle.getVin().equals(ttVehicleDTO.getVin()))
            {
                if(ttVehicleRepository.existsByVinAndDeletedIsFalse(ttVehicleDTO.getVin()))
                {
                    String message="Vehicle with this VIN already exists! VIN: "+ttVehicleDTO.getVin();
                    logger.warn("Vehicle update failed due to VIN conflict: {}",message);
                    throw new VehicleAlreadyExistsException("Vehicle with this vin already exists. Vin: "+ttVehicleDTO.getVin());
                }
                vehicle.setVin(ttVehicleDTO.getVin());
                logger.debug("Vehicle vin updated: {}",vehicle.getVin());
            }
            if(ttVehicleDTO.getModel()!=null&&!vehicle.getModel().equals(ttVehicleDTO.getModel()))
            {
                vehicle.setModel(ttVehicleDTO.getModel());
                logger.debug("Vehicle model updated: {}",vehicle.getModel());
            }
            if(ttVehicleDTO.getColor()!=null&&!vehicle.getColor().equals(ttVehicleDTO.getColor()))
            {
                vehicle.setColor(ttVehicleDTO.getColor());
                logger.debug("Vehicle color updated: {}",vehicle.getColor());
            }
            if(ttVehicleDTO.getYearOfProduction()!=null&&!vehicle.getYearOfProduction().equals(ttVehicleDTO.getYearOfProduction()))
            {
                vehicle.setYearOfProduction(ttVehicleDTO.getYearOfProduction());
                logger.debug("Vehicle year of production updated: {}",vehicle.getYearOfProduction());
            }
            if(ttVehicleDTO.getEngineType()!=null&&!vehicle.getEngineType().equals(ttVehicleDTO.getEngineType()))
            {
                vehicle.setEngineType(ttVehicleDTO.getEngineType());
                logger.debug("Vehicle engine type updated: {}",vehicle.getEngineType());
            }
            if(ttVehicleDTO.getTransmissionType()!=null&&!vehicle.getTransmissionType().equals(ttVehicleDTO.getTransmissionType()))
            {
                vehicle.setTransmissionType(ttVehicleDTO.getTransmissionType());
                logger.debug("Vehicle transmission type updated: {}",vehicle.getTransmissionType());
            }
            ttVehicleRepository.save(vehicle);
            logger.info("Vehicle updated successfully. ID: {}",id);
            return mapUtil.convertToDTO(vehicle);
        }
        else{
            logger.warn("Vehicle not found! ID: {}",id);
            throw new EntityNotFoundException("Vehicle not found! ID: "+id);
        }
    }

    /**
     * Soft deletes TTVehicle with associated defects and locations, if present.
     *
     * @param vehicleId Vehicle id of the vehicle which will be deleted.
     * @return TTVehicleDTO which represents deleted vehicle
     */
    @Override
    @Transactional
    public TTVehicleDTO deleteVehicle(Long vehicleId) {
        Optional<TTVehicle> optionalTTVehicle = ttVehicleRepository.findById(vehicleId);
        logger.info("Deleting vehicle with ID: {}",vehicleId);
        if (optionalTTVehicle.isPresent()) {
            TTVehicle ttVehicle = optionalTTVehicle.get();
            List<TTVehicleDefect> defect = ttVehicle.getDefect();
            defect.forEach(d -> {
                if(d.getLocation()!=null)
                    d.getLocation().forEach(l -> l.setDeleted(true));
                d.setDeleted(true);
            });
            ttVehicle.setDeleted(true);
            TTVehicleDTO response = mapUtil.convertToDTO(ttVehicle);
            logger.info("Vehicle deleted with ID: {}", vehicleId);
            return response;
        } else {
            logger.warn("Vehicle with ID {} does not exits", vehicleId);
            throw new EntityNotFoundException("Vehicle with ID " + vehicleId + " does not exist.");
        }
    }

}
