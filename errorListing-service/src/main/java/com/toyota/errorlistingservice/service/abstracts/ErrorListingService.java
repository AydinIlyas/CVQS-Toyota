package com.toyota.errorlistingservice.service.abstracts;

import com.toyota.errorlistingservice.dto.PaginationResponse;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * Interface for declaring functions of ErrorListingServiceImpl
 */
public interface ErrorListingService {
    /**
     * Gets vehicles with paging,filtering and sorting
     *
     * @param model            desired vehicle model
     * @param vin              desired vehicle identity number
     * @param engineType       desired engine type
     * @param transmissionType desired transmission type
     * @param color            desired color
     * @param yearOfProduction desired year of production
     * @param page             page number
     * @param size             objects on a page
     * @param sortBy           sorted by Field
     * @param sortOrder        sort Direction
     * @return Mono custom paging response
     */
    Mono<PaginationResponse<Object>> getVehicles(int page, int size, List<String> sortBy,
                                                 String sortOrder, String model, String vin, String yearOfProduction,
                                                 String transmissionType, String engineType, String color);
    /**
     * Gets defects with paging, filtering and sorting.
     *
     * @param page       page number starts from 0
     * @param size       entity amount on a page
     * @param type       desired type of defect
     * @param state      desired state of defect
     * @param reportTime desired reportTime of defect
     * @param reportedBy desired reporter
     * @param vin        desired vehicle id number
     * @param sortOrder  desired sorting direction ASC,DESC
     * @param sortBy     ordered by fields
     * @return Mono Custom paging response
     */
    Mono<PaginationResponse<Object>> getDefects(int page,int size,String type, String state, String reportTime, String reportedBy,
                            String vin, String sortOrder, String sortBy);
    /**
     * Gets processed image
     * @param defectId ID of defect with the image
     * @param format Format of the image (png/jpeg)
     * @param processed Specifies whether the image has been processed.
     * @return byte[] image
     */
    byte[] getImage(Long defectId, String format, boolean processed);
}