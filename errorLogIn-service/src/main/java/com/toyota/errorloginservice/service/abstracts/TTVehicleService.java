package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.dto.TTVehicleResponse;

import java.util.List;

/**
 * Interface for TTVehicle's service class.
 */
public interface TTVehicleService {
    List<TTVehicleResponse> getAll();
    TTVehicleResponse addVehicle(TTVehicleDTO ttVehicleDTO);
    TTVehicleResponse deleteVehicle(Long vehicleId);

    TTVehicleResponse updateVehicle(Long id,TTVehicleDTO ttVehicleDTO);
}
