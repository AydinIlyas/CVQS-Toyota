package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Interface for ttVehicleDefect's service class
 */
public interface TTVehicleDefectService {
    TTVehicleDefectResponse addDefect(HttpServletRequest request, Long vehicleId, TTVehicleDefectDTO defectDTO);
    void deleteDefect(Long defectId);

}
