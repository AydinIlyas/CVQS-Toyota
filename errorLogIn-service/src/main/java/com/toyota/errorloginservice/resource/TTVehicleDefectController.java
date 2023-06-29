package com.toyota.errorloginservice.resource;


import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.util.List;


/**
 * Controller for handling tt_vehicle_defect related requests.
 */
@RestController
@RequestMapping("/ttvehicleDefect")
@RequiredArgsConstructor
public class TTVehicleDefectController {
    private final TTVehicleDefectService ttVehicleDefectService;

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
     * Adding defect to tt_vehicle
     * @param vehicleId Vehicle id of the vehicle which has the defect.
     * @param defectDTO The defect of the vehicle.
     * @return ResponseEntity with a message if it was successfully or not
     */
    @PostMapping("/add")
    public ResponseEntity<TTVehicleDefectDTO> addDefect(@RequestHeader("Username") String username,
                                                        @RequestParam Long vehicleId,
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
                                                     @RequestBody TTVehicleDefectDTO defectDTO)
    {
        return ResponseEntity.ok().body(ttVehicleDefectService.update(id,defectDTO));
    }
    /**
     * Soft deletes defect and all associated locations.
     * @param defectId Defect id of the defect that should be deleted.
     * @return ResponseEntity<TTVehicleDefectDTO></> with soft deleted entity.
     */
    @PutMapping("/delete")
    public ResponseEntity<TTVehicleDefectDTO> deleteDefect(@RequestBody Long defectId)
    {
        TTVehicleDefectDTO response=ttVehicleDefectService.deleteDefect(defectId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/add/image/{defectId}")
    public ResponseEntity<Entity> addImage(@PathVariable("defectId") Long defectId,
                                           @RequestParam("Image") MultipartFile image)
    {
        ttVehicleDefectService.addImage(defectId,image);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @GetMapping("/get/image/{defectId}")
    public ResponseEntity<byte[]> getImage(@PathVariable("defectId") Long defectId,
                                           @RequestParam(defaultValue = "jpeg") String format,
                                           @RequestParam(defaultValue="10")int width,
                                           @RequestParam(defaultValue = "10") int height,
                                           @RequestParam(defaultValue = "#FF0000") String colorHex,
                                           @RequestParam(defaultValue = "true") boolean processed)
    {
        HttpHeaders headers=new HttpHeaders();
        byte[] imageData=ttVehicleDefectService.getImage(defectId,format,width,height,colorHex,processed);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        if (format.equalsIgnoreCase("png")) {
            headers.setContentType(MediaType.IMAGE_PNG);
        } else {
            headers.setContentType(MediaType.IMAGE_JPEG);
        }
        return new ResponseEntity<>(imageData,headers,HttpStatus.OK);
    }
}
