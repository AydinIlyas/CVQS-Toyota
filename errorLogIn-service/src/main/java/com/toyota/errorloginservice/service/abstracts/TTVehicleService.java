package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import java.util.List;

/**
 * Interface for TTVehicle's service class.
 */
public interface TTVehicleService {
    PaginationResponse<TTVehicleDTO> getVehiclesFiltered(int page, int size, List<String> sortBy, String sortOrder,
                                                         String model, String vin, String yearOfProduction,
                                                         String transmissionType, String engineType, String color);
    TTVehicleDTO addVehicle(TTVehicleDTO ttVehicleDTO);
    TTVehicleDTO deleteVehicle(Long vehicleId);

    TTVehicleDTO updateVehicle(Long id,TTVehicleDTO ttVehicleDTO);
}
