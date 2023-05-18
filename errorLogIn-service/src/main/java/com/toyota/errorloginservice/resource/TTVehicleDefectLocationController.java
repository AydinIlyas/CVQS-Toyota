package com.toyota.errorloginservice.resource;

import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectLocationService;
import jakarta.validation.Valid;
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
    private ResponseEntity<String> addLocation(@RequestParam Long defectId,
                                                        @RequestBody @Valid TTVehicleDefectLocationDTO location)
    {
        defectLocationService.add(defectId,location);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created Location Successfully!");
    }
    @PutMapping("/delete")
    private ResponseEntity<String> deleteLocation(@RequestBody Long locationId)
    {
        defectLocationService.delete(locationId);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted Location Successfully");
    }


}
