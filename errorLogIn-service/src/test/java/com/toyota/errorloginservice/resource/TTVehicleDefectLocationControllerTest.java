package com.toyota.errorloginservice.resource;

import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectLocationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.parser.Entity;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class TTVehicleDefectLocationControllerTest {
    @Mock
    private TTVehicleDefectLocationService locationService;

    @InjectMocks
    private TTVehicleDefectLocationController locationController;
    @Test
    void addLocation()
    {
        //given
        TTVehicleDefectLocationDTO locationDTO=new TTVehicleDefectLocationDTO();
        Long defectId=1L;

        //when
        ResponseEntity<Entity> response=locationController.addLocation(defectId,locationDTO);

        //then
        Mockito.verify(locationService).add(Mockito.anyLong(),Mockito.any(TTVehicleDefectLocationDTO.class));

        assertEquals(HttpStatus.CREATED,response.getStatusCode());

    }
    @Test
    void deleteLocation()
    {
        //given
        Long locationId=1L;

        //when
        ResponseEntity<Entity> response=locationController.deleteLocation(locationId);

        //then
        Mockito.verify(locationService).delete(locationId);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
}