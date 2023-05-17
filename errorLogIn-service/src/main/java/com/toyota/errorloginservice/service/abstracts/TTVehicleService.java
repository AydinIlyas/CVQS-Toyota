package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.domain.TTVehicle;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.dto.TTVehicleResponse;

import java.util.List;

public interface TTVehicleService {
    List<TTVehicleResponse> getAll();
    TTVehicleResponse addVehicle(TTVehicleDTO ttVehicleDTO);
    TTVehicleResponse deleteVehicle(Long vehicleId);

}
