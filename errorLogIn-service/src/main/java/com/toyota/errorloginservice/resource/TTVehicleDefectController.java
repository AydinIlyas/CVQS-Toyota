package com.toyota.errorloginservice.resource;


import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(defaultValue = "ASC") Sort.Direction sortOrder,
            @RequestParam(defaultValue ="") List<String> sortBy


            )
    {
        return ttVehicleDefectService.getAllFiltered(page,size,type, state, reportTime, reportedBy,
                vin, sortOrder.toString(), sortBy);
    }

    /**
     * Adding defect to tt_vehicle
     * @param vehicleId Vehicle id of the vehicle which has the defect.
     * @param defectDTO The defect of the vehicle.
     * @return ResponseEntity with a message if it was successfully or not
     */
    @PostMapping("/add")
    public ResponseEntity<TTVehicleDefectDTO> addDefect(HttpServletRequest request, @RequestParam Long vehicleId, @RequestBody @Valid TTVehicleDefectDTO defectDTO)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ttVehicleDefectService.addDefect(request,vehicleId,defectDTO));
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
     * @return ResponseEntity with String message.
     */
    @PutMapping("/delete")
    public ResponseEntity<Entity> deleteDefect(@RequestBody Long defectId)
    {
        ttVehicleDefectService.deleteDefect(defectId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
