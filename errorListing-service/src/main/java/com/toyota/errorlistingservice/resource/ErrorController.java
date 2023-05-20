package com.toyota.errorlistingservice.resource;

import com.toyota.errorlistingservice.service.ErrorListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/errors")
@RequiredArgsConstructor
public class ErrorController {
    private final ErrorListingService service;
//    @GetMapping("/getAll")
//    public List<TTVehicle> getAll()
//    {
//        return service.getAll();
//    }


}
