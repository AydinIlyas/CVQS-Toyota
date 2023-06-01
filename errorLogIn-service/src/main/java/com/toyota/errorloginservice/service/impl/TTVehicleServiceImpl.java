package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleRepository;
import com.toyota.errorloginservice.domain.TTVehicle;
import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.exception.EntityNotFoundException;
import com.toyota.errorloginservice.service.abstracts.TTVehicleService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;
    private final Logger logger = LogManager.getLogger(TTVehicleService.class);


    /**
     * @param sortBy Sorted By field
     * @param sortOrder Sort Order (ASC/DESC)
     * @param model desired model
     * @param vin desired vin
     * @param yearOfProduction desired year of production
     * @param transmissionType desired transmissiontype
     * @param engineType desired engineType
     * @param color desired color
     * @return List<TTVehicleDTO>
     */
    @Override
    public List<TTVehicleDTO> getVehiclesFiltered(String sortBy, Sort.Direction sortOrder,
                                                  String model, String vin, String yearOfProduction,
                                                  String transmissionType, String engineType, String color) {
        Sort sort=Sort.by(sortOrder,sortBy);
        List<TTVehicleDTO> ttVehicles= ttVehicleRepository.getVehiclesFiltered(model,vin,yearOfProduction,transmissionType,engineType
        ,color,sort).stream().map(this::convertAllToDTO).collect(Collectors.toList());
        return ttVehicles;
    }

    /**
     * Adds TTVehicle to database.
     *
     * @param ttVehicleDTO Vehicle object which will be added to database.
     * @return TTVehicle Response which represents the added vehicle.
     */
    @Override
    public TTVehicleDTO addVehicle(TTVehicleDTO ttVehicleDTO) {
        TTVehicle ttVehicle = convertToVehicle(ttVehicleDTO);
        TTVehicle saved = ttVehicleRepository.save(ttVehicle);
        logger.info("Successfully added Vehicle!");
        return modelMapper.map(saved, TTVehicleDTO.class);
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
            return convertToDTO(vehicle);
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
                d.getLocation().forEach(l -> l.setDeleted(true));
                d.setDeleted(true);
            });
            ttVehicle.setDeleted(true);
            TTVehicleDTO response = convertToDTO(ttVehicle);
            logger.info("Soft Deleted Vehicle with ID: {}", vehicleId);
            return response;
        } else {
            logger.warn("Vehicle with ID {} does not exits", vehicleId);

            throw new EntityNotFoundException("Vehicle with ID " + vehicleId + " does not exist.");
        }
    }



    /**
     * Converts ttVehicle to ttVehicleResponse.
     *
     * @param ttVehicle Vehicle object which will be converted to Response
     * @return TTVehicleResponse
     */
    private TTVehicleDTO convertToDTO(TTVehicle ttVehicle) {
        return modelMapper.map(ttVehicle, TTVehicleDTO.class);
    }
    private TTVehicleDTO convertAllToDTO(TTVehicle ttVehicle)
    {
        TTVehicleDTO vehicleResponse=modelMapper.map(ttVehicle, TTVehicleDTO.class);
        if(ttVehicle.getDefect()!=null&&!ttVehicle.getDefect().isEmpty())
        {
            List<TTVehicleDefectDTO> defectResponses=ttVehicle.getDefect()
                    .stream().map(this::convertToDefectDTO).collect(Collectors.toList());
            vehicleResponse.setDefect(defectResponses);
        }
        return vehicleResponse;
    }
    private TTVehicleDefectDTO convertToDefectDTO(TTVehicleDefect defect)
    {
        return modelMapper.map(defect, TTVehicleDefectDTO.class);
    }

    private TTVehicle convertToVehicle(TTVehicleDTO ttVehicleDTO)
    {
        return modelMapper.map(ttVehicleDTO,TTVehicle.class);
    }

}
