package com.toyota.errorlistingservice.resource;

import com.toyota.errorlistingservice.dto.TTVehicleResponse;
import com.toyota.errorlistingservice.service.impl.ErrorListingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/errors")
@RequiredArgsConstructor
public class ErrorController {
    private final ErrorListingServiceImpl service;
    @GetMapping("/getAll")
    public List<TTVehicleResponse> getAll(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String attribute,
            @RequestParam(required = false) String desired

    )
    {
        return service.getAll(sort,direction,page,size,attribute,desired);



    }


}
