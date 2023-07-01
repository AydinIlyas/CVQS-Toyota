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
     * @param sortBy Sorted By field
     * @param sortOrder Sort Order (ASC/DESC)
     * @param model desired model
     * @param vin desired vin
     * @param yearOfProduction desired year of production
     * @param transmissionType desired transmission type
     * @param engineType desired engineType
     * @param color desired color
     * @return List<TTVehicleDTO>
     */
    @Override
    public PaginationResponse<TTVehicleDTO> getVehiclesFiltered(int page,int size,List<String> sortBy, String sortOrder,
                                                  String model, String vin, String yearOfProduction,
                                                  String transmissionType, String engineType, String color) {
        Pageable pageable= PageRequest.of(page,size,Sort.by(SortUtil.createSortOrder(sortBy,sortOrder)));
        Page<TTVehicle> pageResponse=ttVehicleRepository.getVehiclesFiltered(model,vin,yearOfProduction,
                transmissionType,engineType
        ,color,pageable);
        List<TTVehicleDTO>ttVehicleDTOS=pageResponse.stream().map(
                mapUtil::convertVehicleWithAllToDTO
        ).collect(Collectors.toList());
        return new PaginationResponse<>(ttVehicleDTOS,pageResponse);
    }


    /**
     * Adds TTVehicle to database.
     *
     * @param ttVehicleDTO Vehicle object which will be added to database.
     * @return TTVehicle Response which represents the added vehicle.
     */
    @Override
    public TTVehicleDTO addVehicle(TTVehicleDTO ttVehicleDTO) {
        if(ttVehicleRepository.existsByVinAndDeletedIsFalse(ttVehicleDTO.getVin()))
        {
            throw new VehicleAlreadyExistsException("Vehicle with this vin already exists. Vin: "+ttVehicleDTO.getVin());
        }
        TTVehicle ttVehicle = mapUtil.convertVehicleDTOToEntity(ttVehicleDTO);
        TTVehicle saved = ttVehicleRepository.save(ttVehicle);
        logger.info("Successfully added Vehicle! ID: {}",saved.getId());
        return mapUtil.convertVehicleWithAllToDTO(saved);
    }

    /**
     * Updates Vehicle
     * @param id ID of vehicle
     * @param ttVehicleDTO Updated vehicle
     * @return TTVehicleResponse
     */
    @Override
    public TTVehicleDTO updateVehicle(Long id,TTVehicleDTO ttVehicleDTO)
    {
        Optional<TTVehicle> optionalTTVehicle=ttVehicleRepository.findById(id);
        if(optionalTTVehicle.isPresent())
        {
            TTVehicle vehicle=optionalTTVehicle.get();

            if(ttVehicleDTO.getModel()!=null&&!vehicle.getModel().equals(ttVehicleDTO.getModel()))
            {
                vehicle.setModel(ttVehicleDTO.getModel());
            }
            if(ttVehicleDTO.getVin()!=null&&!vehicle.getVin().equals(ttVehicleDTO.getVin()))
            {
                if(ttVehicleRepository.existsByVinAndDeletedIsFalse(ttVehicleDTO.getVin()))
                {
                    throw new VehicleAlreadyExistsException("Vehicle with this vin already exists. Vin: "+ttVehicleDTO.getVin());
                }
                vehicle.setVin(ttVehicleDTO.getVin());
            }
            if(ttVehicleDTO.getColor()!=null&&!vehicle.getColor().equals(ttVehicleDTO.getColor()))
            {
                vehicle.setColor(ttVehicleDTO.getColor());
            }
            if(ttVehicleDTO.getYearOfProduction()!=null&&!vehicle.getYearOfProduction().equals(ttVehicleDTO.getYearOfProduction()))
            {
                vehicle.setYearOfProduction(ttVehicleDTO.getYearOfProduction());
            }
            if(ttVehicleDTO.getEngineType()!=null&&!vehicle.getEngineType().equals(ttVehicleDTO.getEngineType()))
            {
                vehicle.setEngineType(ttVehicleDTO.getEngineType());
            }
            if(ttVehicleDTO.getTransmissionType()!=null&&!vehicle.getTransmissionType().equals(ttVehicleDTO.getTransmissionType()))
            {
                vehicle.setTransmissionType(ttVehicleDTO.getTransmissionType());
            }
            ttVehicleRepository.save(vehicle);
            logger.info("UPDATED VEHICLE SUCCESSFULLY! ID: {}",id);
            return mapUtil.convertToDTO(vehicle);
        }
        else{
            logger.warn("VEHICLE NOT FOUND! Id: {}",id);
            throw new EntityNotFoundException("VEHICLE NOT FOUND! ID: "+id);
        }
    }

    /**
     * Soft deletes TTVehicle and associated defects and locations, if present.
     *
     * @param vehicleId Vehicle id of the vehicle which will be deleted.
     */
    @Override
    @Transactional
    public TTVehicleDTO deleteVehicle(Long vehicleId) {
        Optional<TTVehicle> optionalTTVehicle = ttVehicleRepository.findById(vehicleId);

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
            logger.info("Soft Deleted Vehicle with ID: {}", vehicleId);
            return response;
        } else {
            logger.warn("Vehicle with ID {} does not exits", vehicleId);

            throw new EntityNotFoundException("Vehicle with ID " + vehicleId + " does not exist.");
        }
    }

}
