package com.toyota.errorloginservice.resource;


import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectService;
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
    public ResponseEntity<Entity> addDefect(@RequestParam Long id, @RequestBody TTVehicleDefectDTO defectDTO)
    {
        boolean success=ttVehicleDefectService.addDefect(id,defectDTO);
        if(success)
        {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/delete")
    public ResponseEntity<Entity> deleteDefect(@RequestBody Long id)
    {
        boolean success=ttVehicleDefectService.deleteDefect(id);
        if(success)
        {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
