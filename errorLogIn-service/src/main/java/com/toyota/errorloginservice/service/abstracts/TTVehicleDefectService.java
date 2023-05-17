package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;

public interface TTVehicleDefectService {
    boolean addDefect(Long vehicleId, TTVehicleDefectDTO defectDTO);
    boolean deleteDefect(Long defectId);

}
