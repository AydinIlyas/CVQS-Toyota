package com.toyota.errorloginservice.service.abstracts;


import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;

/**
 * Interface for ttVehicleDefectLocation's service class
 */
public interface TTVehicleDefectLocationService {
    /**
     * Adds location to defect.
     * @param defectId Defect id where the location should be added.
     * @param defectLocationDTO Location object which will be added.
     */
    void add(Long defectId, TTVehicleDefectLocationDTO defectLocationDTO);
    /**
     * Soft deletes the location
     * @param locationId ID of TTVehicleDefectLocation which will be deleted.
     */
    void delete(Long locationId);
    /**
     * @param id ID of TTVehicleDefectLocation
     * @param locationDTO Updated Location
     */
    void update(Long id, TTVehicleDefectLocationDTO locationDTO);
}
