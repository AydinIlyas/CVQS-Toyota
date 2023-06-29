package com.toyota.errorloginservice.service.abstracts;


import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;

/**
 * Interface for ttVehicleDefectLocation's service class
 */
public interface TTVehicleDefectLocationService {
    void add(Long defectId, TTVehicleDefectLocationDTO defectLocationDTO);
    void delete(Long locationId);

    void update(Long id, TTVehicleDefectLocationDTO locationDTO);
}
