package com.toyota.errorlistingservice.service.abstracts;

import com.toyota.errorlistingservice.dto.PaginationResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Interface for declaring functions of ErrorListingServiceImpl
 */
public interface ErrorListingService {
    PaginationResponse<Object> getVehicles(HttpServletRequest request, int page, int size, List<String> sort,
                                           String sortOrder, String model, String vin, String yearOfProduction,
                                           String transmissionType, String engineType, String color);

    PaginationResponse<Object> getDefects(HttpServletRequest request,int page,int size,String type, String state, String reportTime, String reportedBy,
                            String vin, String sortOrder, String sortBy);

    byte[] getImage(String authHeader,Long defectId, String format, int width, int height, String colorHex, boolean processed);
}