package com.toyota.errorloginservice.resource;


import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;

@RestController
@RequestMapping("/ttvehicleDefect")
@RequiredArgsConstructor
public class TTVehicleDefectController {
    private final TTVehicleDefectService ttVehicleDefectService;
    @PostMapping("/add")
    public ResponseEntity<String> addDefect(@RequestParam Long id, @RequestBody @Valid TTVehicleDefectDTO defectDTO)
    {
        ttVehicleDefectService.addDefect(id,defectDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Created Defect Successfully!");
    }

    @PutMapping("/delete")
    public ResponseEntity<String> deleteDefect(@RequestBody Long id)
    {
        ttVehicleDefectService.deleteDefect(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted Defect Successfully!");
    }

}
