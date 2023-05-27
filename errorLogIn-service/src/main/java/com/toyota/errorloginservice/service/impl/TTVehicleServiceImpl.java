package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleRepository;
import com.toyota.errorloginservice.domain.TTVehicle;
import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.dto.TTVehicleResponse;
import com.toyota.errorloginservice.exception.EntityNotFoundException;
import com.toyota.errorloginservice.service.abstracts.TTVehicleService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
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

    @Override
    public List<TTVehicleResponse> getAll() {

        List<TTVehicle> ttVehicles = ttVehicleRepository.findAllByDeletedIsFalse();
        return ttVehicles.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

    }

    /**
     * Adds TTVehicle to database.
     *
     * @param ttVehicleDTO Vehicle object which will be added to database.
     * @return TTVehicle Response which represents the added vehicle.
     */
    @Override
    public TTVehicleResponse addVehicle(TTVehicleDTO ttVehicleDTO) {
        TTVehicle ttVehicle = TTVehicle.builder()
                .name(ttVehicleDTO.getName())
                .introductionDate(ttVehicleDTO.getIntroductionDate())
                .color(ttVehicleDTO.getColor()).build();
        TTVehicle saved = ttVehicleRepository.save(ttVehicle);
        logger.info("Successfully added Vehicle!");
        return modelMapper.map(saved, TTVehicleResponse.class);
    }

    /**
     * Soft deletes TTVehicle and associated defects and locations, if present.
     *
     * @param vehicleId Vehicle id of the vehicle which will be deleted.
     */
    @Override
    @Transactional
    public TTVehicleResponse deleteVehicle(Long vehicleId) {
        Optional<TTVehicle> optionalTTVehicle = ttVehicleRepository.findById(vehicleId);

        if (optionalTTVehicle.isPresent()) {
            TTVehicle ttVehicle = optionalTTVehicle.get();
            List<TTVehicleDefect> defect = ttVehicle.getDefect();
            defect.forEach(d -> {
                d.getLocation().forEach(l -> l.setDeleted(true));
                d.setDeleted(true);
            });
            ttVehicle.setDeleted(true);
            TTVehicleResponse response = convertToResponse(ttVehicle);
            logger.info("Soft Deleted Vehicle with id: {}", vehicleId);
            return response;
        } else {
            logger.warn("Vehicle with id {} does not exits", vehicleId);

            throw new EntityNotFoundException("Vehicle with id " + vehicleId + " does not exist.");
        }
    }

    /**
     * Converts ttVehicle to ttVehicleResponse.
     *
     * @param ttVehicle Vehicle object which will be converted to Response
     * @return TTVehicleResponse
     */
    private TTVehicleResponse convertToResponse(TTVehicle ttVehicle) {
        return modelMapper.map(ttVehicle, TTVehicleResponse.class);
    }

}
