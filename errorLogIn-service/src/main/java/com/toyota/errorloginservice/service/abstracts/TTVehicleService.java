package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.dto.TTVehicleDTO;
import org.springframework.data.domain.Sort;
import java.util.List;

/**
 * Interface for TTVehicle's service class.
 */
public interface TTVehicleService {
    List<TTVehicleDTO> getVehiclesFiltered(String sortBy, Sort.Direction sortOrder,
                                           String model, String vin, String yearOfProduction,
                                           String transmissionType, String engineType, String color);
    TTVehicleDTO addVehicle(TTVehicleDTO ttVehicleDTO);
    TTVehicleDTO deleteVehicle(Long vehicleId);

    TTVehicleDTO updateVehicle(Long id,TTVehicleDTO ttVehicleDTO);
}
