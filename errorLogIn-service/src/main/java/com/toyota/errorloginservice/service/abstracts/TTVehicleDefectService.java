package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Interface for ttVehicleDefect's service class
 */
public interface TTVehicleDefectService {
    TTVehicleDefectDTO addDefect(HttpServletRequest request, Long vehicleId, TTVehicleDefectDTO defectDTO);
    void deleteDefect(Long defectId);

    TTVehicleDefectDTO update(Long id, TTVehicleDefectDTO defectDTO);
}
