package com.toyota.errorloginservice.resource;

import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.dto.TTVehicleResponse;
import com.toyota.errorloginservice.service.abstracts.TTVehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling requests related to tt_vehicle.
 */
@RestController
@RequestMapping("/ttvehicle")
@RequiredArgsConstructor
public class TTVehicleController {
    private final TTVehicleService ttVehicleService;

    @GetMapping("/getAll")
    public List<TTVehicleResponse> getAllVehicles()
    {
        return ttVehicleService.getAll();
    }

    /**
     * Adding a new vehicle to the database.
     * @param ttVehicleDTO Vehicle that should be added
     * @return A ResponseEntity with the added TTVehicleResponse and HTTP status
     */
    @PostMapping("/addVehicle")
    public ResponseEntity<TTVehicleResponse> addVehicle(@RequestBody @Valid TTVehicleDTO ttVehicleDTO)
    {
        TTVehicleResponse response=ttVehicleService.addVehicle(ttVehicleDTO);

        if(response==null)
        {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Soft deletes tt_vehicle and all associated defects and locations from the database.
     * @param vehicleId Vehicle id that should be deleted
     * @return ResponseEntity with the deleted TTVehicleResponse
     */
    @PutMapping("/deleteVehicle")
    public ResponseEntity<TTVehicleResponse> deleteVehicle(@RequestBody Long vehicleId)
    {
        TTVehicleResponse vehicleResponse=ttVehicleService.deleteVehicle(vehicleId);
        if(vehicleResponse==null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehicleResponse);
    }

}
