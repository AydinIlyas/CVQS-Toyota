package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;

public interface TTVehicleDefectLocationService {
    boolean add(Long defectId, TTVehicleDefectLocationDTO defectLocationDTO);
    boolean delete(Long locationId);
}
