package com.toyota.errorlistingservice.resource;


import com.toyota.errorlistingservice.dto.PaginationResponse;
import com.toyota.errorlistingservice.service.impl.ErrorListingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * Controller class for handling requests related to list vehicles.
 */
@RestController
@RequestMapping("/errors")
@RequiredArgsConstructor
public class ErrorController {
    private final ErrorListingServiceImpl service;

    /**
     * Request for getting all vehicles with paging,filtering and sorting
     * @param filterModel desired vehicle model
     * @param filterVin desired vehicle identity number
     * @param filterEngineType desired engine type
     * @param filterTransmissionType desired transmission type
     * @param filterColor desired color
     * @param filterYearOfProduction desired year of production
     * @param page page number
     * @param size objects on a page
     * @param sortBy sorted by Field
     * @param sortOrder sort Direction
     * @return vehicle objects
     */
    @GetMapping("/getVehicles")
    public Mono<PaginationResponse<Object>> getAllVehiclesFiltered(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "5") int size,
                                                                   @RequestParam(defaultValue = "") List<String> sortBy,
                                                                   @RequestParam(defaultValue = "ASC") String sortOrder,
                                                                   @RequestParam(defaultValue = "") String filterModel,
                                                                   @RequestParam(defaultValue = "") String filterVin,
                                                                   @RequestParam(defaultValue = "") String filterYearOfProduction,
                                                                   @RequestParam(defaultValue = "") String filterTransmissionType,
                                                                   @RequestParam(defaultValue = "") String filterEngineType,
                                                                   @RequestParam(defaultValue = "") String filterColor
    )
    {
        return service.getVehicles(page, size, sortBy, sortOrder,
                filterModel, filterVin, filterYearOfProduction, filterTransmissionType,
                filterEngineType, filterColor);
    }

    /**
     * Request for getting vehicles with paging, filtering and sorting
     * @param page page Number starts from 0
     * @param size entity amount on a page
     * @param type desired type of defect
     * @param state desired state of defect
     * @param reportTime desired reportTime of defect
     * @param reportedBy desired reporter
     * @param vin desired vehicle id number
     * @param sortOrder desired sorting direction ASC,DESC
     * @param sortBy ordered by fields
     * @return Custom paging response
     */
    @GetMapping("getDefects")
    public Mono<PaginationResponse<Object>> getAllDefectsFiltered(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "") String state,
            @RequestParam(defaultValue = "") String reportTime,
            @RequestParam(defaultValue = "") String reportedBy,
            @RequestParam(defaultValue = "") String vin,
            @RequestParam(defaultValue = "ASC") String sortOrder,
            @RequestParam(defaultValue = "") String sortBy


    )
    {
        return service.getDefects(page,size,type, state, reportTime, reportedBy,
                vin, sortOrder, sortBy);
    }

    @GetMapping("/get/image/{defectId}")
    public ResponseEntity<byte[]> getImage(@PathVariable("defectId") Long defectId,
                                           @RequestParam(defaultValue = "jpeg") String format,
                                           @RequestParam(defaultValue="10")int width,
                                           @RequestParam(defaultValue = "10") int height,
                                           @RequestParam(defaultValue = "#FF0000") String colorHex,
                                           @RequestParam(defaultValue = "true") boolean processed)
    {
        HttpHeaders headers=new HttpHeaders();
        byte[] imageData=service.getImage(defectId,format,width,height,colorHex,processed);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        if (format.equalsIgnoreCase("png")) {
            headers.setContentType(MediaType.IMAGE_PNG);
        } else {
            headers.setContentType(MediaType.IMAGE_JPEG);
        }
        return new ResponseEntity<>(imageData,headers, HttpStatus.OK);
    }


}
