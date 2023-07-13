package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import java.util.List;

/**
 * Interface for TTVehicle's service class.
 */
public interface TTVehicleService {
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
    PaginationResponse<TTVehicleDTO> getVehiclesFiltered(int page, int size, List<String> sortBy, String sortOrder,
                                                         String model, String vin, String yearOfProduction,
                                                         String transmissionType, String engineType, String color);
    /**
     * Adds TTVehicle to database.
     *
     * @param ttVehicleDTO Vehicle object which will be added to database.
     * @return TTVehicleDTO which represents the added vehicle.
     */
    TTVehicleDTO addVehicle(TTVehicleDTO ttVehicleDTO);
    /**
     * Soft deletes TTVehicle with associated defects and locations, if present.
     *
     * @param vehicleId Vehicle id of the vehicle which will be deleted.
     * @return TTVehicleDTO which represents deleted vehicle
     */

    TTVehicleDTO deleteVehicle(Long vehicleId);
    /**
     * Updates Vehicle
     * @param id ID of vehicle
     * @param ttVehicleDTO Updated vehicle
     * @return TTVehicleDTO which represents updated vehicle
     */

    TTVehicleDTO updateVehicle(Long id,TTVehicleDTO ttVehicleDTO);
}
