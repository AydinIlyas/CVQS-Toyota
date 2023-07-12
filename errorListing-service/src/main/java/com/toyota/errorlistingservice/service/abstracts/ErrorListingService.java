package com.toyota.errorlistingservice.service.abstracts;

import com.toyota.errorlistingservice.dto.PaginationResponse;
import jakarta.servlet.http.HttpServletRequest;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * Interface for declaring functions of ErrorListingServiceImpl
 */
public interface ErrorListingService {
    Mono<PaginationResponse<Object>> getVehicles(int page, int size, List<String> sort,
                                                 String sortOrder, String model, String vin, String yearOfProduction,
                                                 String transmissionType, String engineType, String color);

    Mono<PaginationResponse<Object>> getDefects(int page,int size,String type, String state, String reportTime, String reportedBy,
                            String vin, String sortOrder, String sortBy);

    byte[] getImage(Long defectId, String format, int width, int height, String colorHex, boolean processed);
}