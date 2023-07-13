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
@RequestMapping("/location")
@RequiredArgsConstructor
public class TTVehicleDefectLocationController {
    private final TTVehicleDefectLocationService defectLocationService;

    /**
     * Adds Location to defect.
     * @param defectId ID of defect
     * @param location TTVehicleDefectLocationDTO which should be added to the defect.
     * @return ResponseEntity with matching status
     */
    @PostMapping("/add/{defectId}")
    public ResponseEntity<Entity> addLocation(@PathVariable("defectId") Long defectId,
                                                        @RequestBody @Valid TTVehicleDefectLocationDTO location)
    {
        defectLocationService.add(defectId,location);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Updates location
     * @param id ID of location which will be updated
     * @param locationDTO updated location.
     * @return ResponseEntity with matching status
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Entity> update(@PathVariable("id") Long id,
                                                     @Validated(UpdateValidation.class)
                                                     @RequestBody TTVehicleDefectLocationDTO locationDTO)
    {
        defectLocationService.update(id,locationDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    /**
     * Soft deletes location from database.
     * @param locationId ID of  location that should be deleted.
     * @return ResponseEntity with matching status
     */
    @PutMapping("/delete")
    public ResponseEntity<Entity> deleteLocation(@RequestBody Long locationId)
    {
        defectLocationService.delete(locationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
