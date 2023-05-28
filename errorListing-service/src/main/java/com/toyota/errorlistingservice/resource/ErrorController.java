package com.toyota.errorlistingservice.resource;

import com.toyota.errorlistingservice.dto.TTVehicleResponse;
import com.toyota.errorlistingservice.service.impl.ErrorListingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * Calls function for collecting all errors.
     * @param request Request
     * @param sort Determines which field is sorted by.
     * @param direction Determines the direction in which to sort (asc/desc).
     * @param page Determines which page it should show.
     * @param size Determines how many objects should in one page.
     * @param attribute Determines what field you are looking for.
     * @param desired The string you are looking for.
     * @return List of vehicles.
     */
    @GetMapping("/getAll")
    public List<TTVehicleResponse> getAll(HttpServletRequest request,
                                          @RequestParam(required = false) String sort,
                                          @RequestParam(required = false) String direction,
                                          @RequestParam(required = false) Integer page,
                                          @RequestParam(required = false) Integer size,
                                          @RequestParam(required = false) String attribute,
                                          @RequestParam(required = false) String desired

    )
    {
        return service.getAll(request,sort,direction,page,size,attribute,desired);



    }


}
