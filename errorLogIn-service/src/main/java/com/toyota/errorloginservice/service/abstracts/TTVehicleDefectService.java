package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;

public interface TTVehicleDefectService {
    void addDefect(Long vehicleId, TTVehicleDefectDTO defectDTO);
    void deleteDefect(Long defectId);

}
