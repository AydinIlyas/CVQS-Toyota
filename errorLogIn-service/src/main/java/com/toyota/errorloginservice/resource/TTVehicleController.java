package com.toyota.errorloginservice.resource;

import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.dto.UpdateValidation;
import com.toyota.errorloginservice.service.abstracts.TTVehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    /**
     * @param model desired model
     * @param vin desired vin
     * @param engineType desired engineType
     * @param transmissionType desired transmissionType
     * @param color desired color
     * @param yearOfProduction desired year of Production
     * @param sortBy Sorted By field
     * @param sortOrder Sort Order (ASC/DESC)
     * @return List<TTVehicleDTO>
     */
    @GetMapping("/getAll")
    public PaginationResponse<TTVehicleDTO> getAllVehicles(@RequestParam(defaultValue = "") int page,
                                                           @RequestParam(defaultValue = "") int size,
                                                           @RequestParam(defaultValue = "") String model,
                                                           @RequestParam(defaultValue = "") String vin,
                                                           @RequestParam(defaultValue = "") String engineType,
                                                           @RequestParam(defaultValue = "") String transmissionType,
                                                           @RequestParam(defaultValue = "") String color,
                                                           @RequestParam(defaultValue = "") String yearOfProduction,
                                                           @RequestParam(defaultValue = "") List<String> sortBy,
                                                           @RequestParam(defaultValue = "ASC") String sortOrder)
    {
        return ttVehicleService.getVehiclesFiltered(page,size,sortBy, sortOrder, model,
                vin, yearOfProduction, transmissionType, engineType, color);
    }

    /**
     * Adding a new vehicle to the database.
     * @param ttVehicleDTO Vehicle that should be added
     * @return A ResponseEntity with the added TTVehicleResponse and HTTP status
     */
    @PostMapping("/addVehicle")
    public ResponseEntity<TTVehicleDTO> addVehicle(@RequestBody @Valid TTVehicleDTO ttVehicleDTO)
    {
        TTVehicleDTO response=ttVehicleService.addVehicle(ttVehicleDTO);

        if(response==null)
        {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Updates Vehicle
     * @param id ID of vehicle
     * @param ttVehicleDTO Updated vehicle
     * @return ResponseEntity<TTVehicleResponse>
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<TTVehicleDTO> updateVehicle(@PathVariable("id") Long id,
                                                        @Validated(UpdateValidation.class)  @RequestBody TTVehicleDTO ttVehicleDTO)
    {
        TTVehicleDTO response=ttVehicleService.updateVehicle(id,ttVehicleDTO);
        if(response==null)
        {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    /**
     * Soft deletes tt_vehicle and all associated defects and locations from the database.
     * @param vehicleId Vehicle id that should be deleted
     * @return ResponseEntity with the deleted TTVehicleResponse
     */
    @PutMapping("/deleteVehicle")
    public ResponseEntity<TTVehicleDTO> deleteVehicle(@RequestBody Long vehicleId)
    {
        TTVehicleDTO vehicleResponse=ttVehicleService.deleteVehicle(vehicleId);
        if(vehicleResponse==null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehicleResponse);
    }

}
