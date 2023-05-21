package com.toyota.errorlistingservice.service.abstracts;

import com.toyota.errorlistingservice.dto.TTVehicleResponse;

import java.util.List;

public interface ErrorListingService {
    List<TTVehicleResponse> getAll(String sort,String direction,Integer page, Integer size,String attribute,String desiredValue);
}
