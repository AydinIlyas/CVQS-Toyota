package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.dto.TTVehicleDTO;

import java.util.List;

/**
 * Interface for TTVehicle's service class.
 */
public interface TTVehicleService {
    List<TTVehicleDTO> getAll();
    TTVehicleDTO addVehicle(TTVehicleDTO ttVehicleDTO);
    TTVehicleDTO deleteVehicle(Long vehicleId);

    TTVehicleDTO updateVehicle(Long id,TTVehicleDTO ttVehicleDTO);
}
