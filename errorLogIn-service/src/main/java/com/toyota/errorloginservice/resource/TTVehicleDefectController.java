package com.toyota.errorloginservice.resource;


import com.toyota.errorloginservice.dto.ImageDTO;
import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.dto.UpdateValidation;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.util.List;


/**
 * Controller for handling tt_vehicle_defect related requests.
 */
@RestController
@RequestMapping("/defect")
@RequiredArgsConstructor
public class TTVehicleDefectController {
    private final TTVehicleDefectService ttVehicleDefectService;

    /**
     * Gets Defects with paging, sorting and filtering
     * @param page desired page
     * @param size desired size of page
     * @param type desired type
     * @param state desired state
     * @param reportTime report time of defect
     * @param reportedBy username of reporter
     * @param vin desired Vehicle Identification number
     * @param sortOrder desired order of the page (ASC/DESC)
     * @param sortBy desired field to sort by
     * @return TTVehicleDefectDTO
     */
    @GetMapping("getAll")
    public PaginationResponse<TTVehicleDefectDTO> getAllFiltered(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue ="5") int size,
            @RequestParam(defaultValue ="") String type,
            @RequestParam(defaultValue ="") String state,
            @RequestParam(defaultValue ="") String reportTime,
            @RequestParam(defaultValue ="") String reportedBy,
            @RequestParam(defaultValue ="") String vin,
            @RequestParam(defaultValue = "ASC") String sortOrder,
            @RequestParam(defaultValue ="") List<String> sortBy


            )
    {
        return ttVehicleDefectService.getAllFiltered(page,size,type, state, reportTime, reportedBy,
                vin, sortOrder, sortBy);
    }

    /**
     * Adds defect to tt_vehicle
     * @param vehicleId Vehicle id of the vehicle which has the defect.
     * @param defectDTO The defect of the vehicle.
     * @return ResponseEntity with a message if it was successfully or not
     */
    @PostMapping("/add/{vehicleId}")
    public ResponseEntity<TTVehicleDefectDTO> addDefect(@RequestHeader("Username") String username,
                                                        @PathVariable("vehicleId") Long vehicleId,
                                                        @RequestBody @Valid TTVehicleDefectDTO defectDTO)
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ttVehicleDefectService.addDefect(username,vehicleId,defectDTO));
    }

    /**
     * Updates defect
     * @param id ID of defect which will be updated
     * @param defectDTO updated defect.
     * @return ResponseEntity<TTVehicleDefectDTO> updated defect
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<TTVehicleDefectDTO> update(@PathVariable("id") Long id,
                                                     @Validated(UpdateValidation.class) @RequestBody TTVehicleDefectDTO defectDTO)
    {
        return ResponseEntity.ok().body(ttVehicleDefectService.update(id,defectDTO));
    }
    /**
     * Soft deletes defect and all associated locations.
     * @param defectId Defect id of the defect that should be deleted.
     * @return ResponseEntity with soft deleted entity.
     */
    @PutMapping("/delete")
    public ResponseEntity<TTVehicleDefectDTO> deleteDefect(@RequestBody Long defectId)
    {
        TTVehicleDefectDTO response=ttVehicleDefectService.deleteDefect(defectId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Adds image to defect
     * @param defectId ID of defect
     * @param image MultiPartFile with the image
     * @return ResponseEntity with a matching status
     */
    @PostMapping("/{defectId}/add/image")
    public ResponseEntity<Entity> addImage(@PathVariable("defectId") Long defectId,
                                           @RequestParam("Image") MultipartFile image)
    {
        ttVehicleDefectService.addImage(defectId,image);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Removes Image from defect
     * @param defectId ID of defect
     * @return ResponseEntity with matching status
     */
    @PutMapping("delete/image")
    public ResponseEntity<Entity> removeImage(@RequestBody Long defectId)
    {
        ttVehicleDefectService.removeImage(defectId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Gets image with locations for processing it later
     * @param defectId ID of defect
     * @return ImageDTO which contains image as bytes and TTVehicleLocationDTO
     */
    @GetMapping("{defectId}/get/image")
    public ImageDTO getImage(@PathVariable("defectId") Long defectId)
    {
        return ttVehicleDefectService.getImage(defectId);
    }
}
