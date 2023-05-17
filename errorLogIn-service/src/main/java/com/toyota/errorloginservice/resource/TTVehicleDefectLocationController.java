package com.toyota.errorloginservice.resource;

import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;

@RestController
@RequestMapping("/defectLocation")
@RequiredArgsConstructor
public class TTVehicleDefectLocationController {
    private final TTVehicleDefectLocationService defectLocationService;
    @PostMapping("/add")
    private ResponseEntity<TTVehicleDefect> addLocation(@RequestParam Long defectId,
                                                        @RequestBody TTVehicleDefectLocationDTO location)
    {
        boolean success=defectLocationService.add(defectId,location);
        if(success)
        {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PutMapping("/delete")
    private ResponseEntity<Entity> deleteLocation(@RequestBody Long locationId)
    {
        boolean success=defectLocationService.delete(locationId);
        if(success)
        {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
