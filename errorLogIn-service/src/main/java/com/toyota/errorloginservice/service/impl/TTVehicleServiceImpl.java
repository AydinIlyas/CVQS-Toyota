package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleRepository;
import com.toyota.errorloginservice.domain.TTVehicle;
import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.dto.TTVehicleResponse;
import com.toyota.errorloginservice.exception.EntityNotFoundException;
import com.toyota.errorloginservice.service.abstracts.TTVehicleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TTVehicleServiceImpl implements TTVehicleService {
    private final TTVehicleRepository ttVehicleRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<TTVehicleResponse> getAll() {

        List<TTVehicle> ttVehicles= ttVehicleRepository.findAllByDeletedIsFalse();
        return ttVehicles.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TTVehicleResponse addVehicle(TTVehicleDTO ttVehicleDTO) {
        TTVehicle ttVehicle=TTVehicle.builder()
                .name(ttVehicleDTO.getName())
                .introductionDate(ttVehicleDTO.getIntroductionDate())
                .color(ttVehicleDTO.getColor()).build();
        TTVehicle saved=ttVehicleRepository.save(ttVehicle);
        if(saved!=null)
        {
            return modelMapper.map(saved,TTVehicleResponse.class);
        }
        return null;
    }

    /**
     * @param vehicleId
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
            return response;
        }
        else{
            throw new EntityNotFoundException("Vehicle does with id "+vehicleId+" does not exist.");
        }
    }

    private TTVehicleResponse convertToResponse(TTVehicle ttVehicle)
    {
        return modelMapper.map(ttVehicle,TTVehicleResponse.class);
    }

}
