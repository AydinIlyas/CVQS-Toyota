package com.toyota.errorloginservice.resource;


import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectResponse;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for handling tt_vehicle_defect related requests.
 */
@RestController
@RequestMapping("/ttvehicleDefect")
@RequiredArgsConstructor
public class TTVehicleDefectController {
    private final TTVehicleDefectService ttVehicleDefectService;

    /**
     * Adding defect to tt_vehicle
     * @param vehicleId Vehicle id of the vehicle which has the defect.
     * @param defectDTO The defect of the vehicle.
     * @return ResponseEntity with a message if it was successfully or not
     */
    @PostMapping("/add")
    public ResponseEntity<TTVehicleDefectResponse> addDefect(HttpServletRequest request, @RequestParam Long vehicleId, @RequestBody @Valid TTVehicleDefectDTO defectDTO)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ttVehicleDefectService.addDefect(request,vehicleId,defectDTO));
    }

    /**
     * Soft deletes defect and all associated locations.
     * @param defectId Defect id of the defect that should be deleted.
     * @return ResponseEntity with String message.
     */
    @PutMapping("/delete")
    public ResponseEntity<String> deleteDefect(@RequestBody Long defectId)
    {
        ttVehicleDefectService.deleteDefect(defectId);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted Defect Successfully!");
    }

}
