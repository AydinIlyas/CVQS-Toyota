package com.toyota.errorloginservice.resource;

import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for tt_vehicle_defect_location related requests.
 */
@RestController
@RequestMapping("/defectLocation")
@RequiredArgsConstructor
public class TTVehicleDefectLocationController {
    private final TTVehicleDefectLocationService defectLocationService;

    /**
     * Adds Location to defect.
     * @param defectId Defect id where the location should be added.
     * @param location Location object which should be added.
     * @return ResponseEntity with String message.
     */
    @PostMapping("/add")
    private ResponseEntity<String> addLocation(@RequestParam Long defectId,
                                                        @RequestBody @Valid TTVehicleDefectLocationDTO location)
    {
        defectLocationService.add(defectId,location);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created Location Successfully!");
    }

    /**
     * Soft deletes location from database.
     * @param locationId Location id of the location that should be deleted.
     * @return ResponseEntity with String message
     */
    @PutMapping("/delete")
    private ResponseEntity<String> deleteLocation(@RequestBody Long locationId)
    {
        defectLocationService.delete(locationId);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted Location Successfully");
    }


}
