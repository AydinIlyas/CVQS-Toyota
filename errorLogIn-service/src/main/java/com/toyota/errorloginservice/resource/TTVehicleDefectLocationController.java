package com.toyota.errorloginservice.resource;

import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorloginservice.dto.UpdateValidation;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;


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
    public ResponseEntity<Entity> addLocation(@RequestParam Long defectId,
                                                        @RequestBody @Valid TTVehicleDefectLocationDTO location)
    {
        defectLocationService.add(defectId,location);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Updates location
     * @param id ID of location which will be updated
     * @param locationDTO updated location.
     * @return ResponseEntity<TTVehicleDefectDTO> updated defect
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<TTVehicleDefectDTO> update(@PathVariable("id") Long id,
                                                     @Validated(UpdateValidation.class)
                                                     @RequestBody TTVehicleDefectLocationDTO locationDTO)
    {
        defectLocationService.update(id,locationDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    /**
     * Soft deletes location from database.
     * @param locationId Location id of the location that should be deleted.
     * @return ResponseEntity with String message
     */
    @PutMapping("/delete")
    public ResponseEntity<Entity> deleteLocation(@RequestBody Long locationId)
    {
        defectLocationService.delete(locationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
