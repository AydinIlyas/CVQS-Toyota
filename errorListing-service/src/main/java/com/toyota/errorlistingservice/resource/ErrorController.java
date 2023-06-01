package com.toyota.errorlistingservice.resource;


import com.toyota.errorlistingservice.service.impl.ErrorListingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
     * @param request request for adding bearer tokens
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
    @GetMapping("/getAll")
    public Page<Object> getAll(HttpServletRequest request,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "") String sortBy,
                                     @RequestParam(defaultValue = "ASC") String sortOrder,
                                     @RequestParam(defaultValue = "") String filterModel,
                                     @RequestParam(defaultValue = "") String filterVin,
                                     @RequestParam(defaultValue = "") String filterYearOfProduction,
                                     @RequestParam(defaultValue = "") String filterTransmissionType,
                                     @RequestParam(defaultValue = "") String filterEngineType,
                                     @RequestParam(defaultValue = "") String filterColor
    )
    {
        return service.getAll(request, page, size, sortBy, sortOrder,
                filterModel, filterVin, filterYearOfProduction, filterTransmissionType,
                filterEngineType, filterColor);



    }


}
