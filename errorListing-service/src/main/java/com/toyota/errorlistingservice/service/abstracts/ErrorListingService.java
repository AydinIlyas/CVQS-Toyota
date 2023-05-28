package com.toyota.errorlistingservice.service.abstracts;

import com.toyota.errorlistingservice.dto.TTVehicleResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * Interface for declaring functions of ErrorListingServiceImpl
 */
public interface ErrorListingService {
    List<TTVehicleResponse> getAll(HttpServletRequest request,String sort, String direction, Integer page, Integer size, String attribute, String desiredValue);
}
