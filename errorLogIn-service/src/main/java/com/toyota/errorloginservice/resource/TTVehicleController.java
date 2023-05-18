package com.toyota.errorloginservice.resource;

import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.dto.TTVehicleResponse;
import com.toyota.errorloginservice.dto.TTVehicleResponse;
import com.toyota.errorloginservice.service.abstracts.TTVehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
