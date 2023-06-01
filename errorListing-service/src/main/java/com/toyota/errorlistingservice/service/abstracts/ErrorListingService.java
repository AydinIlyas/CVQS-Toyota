package com.toyota.errorlistingservice.service.abstracts;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;


/**
 * Interface for declaring functions of ErrorListingServiceImpl
 */
public interface ErrorListingService {
    Page<Object> getAll(HttpServletRequest request, int page, int size, String sort,
                              String sortOrder, String model, String vin, String yearOfProduction,
                              String transmissionType, String engineType, String color);
}