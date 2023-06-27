package com.toyota.errorloginservice.resource;

import com.toyota.errorloginservice.domain.State;
import com.toyota.errorloginservice.dto.CustomPageable;
import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.service.impl.TTVehicleDefectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class TTVehicleDefectControllerTest {
    @Mock
    TTVehicleDefectServiceImpl ttVehicleDefectService;
    @InjectMocks
    TTVehicleDefectController defectController;



    @Test
    void getAllFiltered() {
        //given
        List<TTVehicleDefectDTO> mockDefects=List.of(
                new TTVehicleDefectDTO(),
                new TTVehicleDefectDTO(),
                new TTVehicleDefectDTO()
        );
        CustomPageable customPageable=new CustomPageable(0,5,3,10);
        PaginationResponse<TTVehicleDefectDTO> paginationResponse=new PaginationResponse<>(mockDefects,customPageable);

        //when
        Mockito.when(ttVehicleDefectService.getAllFiltered(anyInt(), anyInt(), anyString(), anyString(),
                        anyString(), anyString(), anyString(), anyString(), anyList()))
                .thenReturn(paginationResponse);

        PaginationResponse<TTVehicleDefectDTO> result=defectController.getAllFiltered(0, 5
                , "","", "", "", "", "ASC",Collections.emptyList()
        );

        //then
        assertEquals(0,result.getPageable().getPageNumber());
        assertEquals(5,result.getPageable().getPageSize());
        assertEquals(3,result.getPageable().getTotalPages());
        assertEquals(10,result.getPageable().getTotalElements());
    }

    @Test
    void addDefect() {
        //given
        TTVehicleDefectDTO defectDTO=new TTVehicleDefectDTO(1L,"Broken Window","Windshield",
                State.MAJOR,null,Collections.emptyList());
        Long vehicleId=1L;
        String username="username";
        //when
        Mockito.when(ttVehicleDefectService.addDefect(anyString(),anyLong(),any(TTVehicleDefectDTO.class)))
                .thenReturn(defectDTO);

        ResponseEntity<TTVehicleDefectDTO> result =defectController.addDefect(username,vehicleId,defectDTO);
        //then
        Mockito.verify(ttVehicleDefectService).addDefect(any(),anyLong(),any(TTVehicleDefectDTO.class));
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        TTVehicleDefectDTO resultBody=result.getBody();
        assertNotNull(resultBody);
        assertEquals(defectDTO.getId(), resultBody.getId());
        assertEquals(defectDTO.getType(), resultBody.getType());
        assertEquals(defectDTO.getDescription(), resultBody.getDescription());
        assertEquals(defectDTO.getState(), resultBody.getState());
        assertEquals(defectDTO.getDefectImage(), resultBody.getDefectImage());
    }

    @Test
    void update() {
        //given
        TTVehicleDefectDTO defectDTO=new TTVehicleDefectDTO();
        Long vehicleId=1L;
        //when
        Mockito.when(ttVehicleDefectService.update(anyLong(),any(TTVehicleDefectDTO.class)))
                .thenReturn(defectDTO);

        ResponseEntity<TTVehicleDefectDTO> result =defectController.update(vehicleId,defectDTO);
        //then
        Mockito.verify(ttVehicleDefectService).update(anyLong(),any(TTVehicleDefectDTO.class));
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void deleteDefect() {
        //given
        TTVehicleDefectDTO defectDTO=new TTVehicleDefectDTO();
        Long vehicleId=1L;

        //when
        Mockito.when(ttVehicleDefectService.deleteDefect(anyLong()))
                .thenReturn(defectDTO);
        ResponseEntity<TTVehicleDefectDTO> result =defectController.deleteDefect(vehicleId);

        //then
        Mockito.verify(ttVehicleDefectService).deleteDefect(anyLong());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}