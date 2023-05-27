package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;

/**
 * Interface for ttVehicleDefect's service class
 */
public interface TTVehicleDefectService {
    void addDefect(Long vehicleId, TTVehicleDefectDTO defectDTO);
    void deleteDefect(Long defectId);

}
