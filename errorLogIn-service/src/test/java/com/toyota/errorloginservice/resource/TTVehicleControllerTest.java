package com.toyota.errorloginservice.resource;

import com.toyota.errorloginservice.domain.EngineType;
import com.toyota.errorloginservice.domain.TransmissionType;
import com.toyota.errorloginservice.dto.CustomPageable;
import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.service.impl.TTVehicleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class TTVehicleControllerTest {

    @Mock
    TTVehicleServiceImpl ttVehicleService;
    @InjectMocks
    TTVehicleController ttVehicleController;


    @Test
    void getAllVehicles() {
        //given
        List<TTVehicleDTO> mockVehicles=List.of(
                new TTVehicleDTO(),
                new TTVehicleDTO(),
                new TTVehicleDTO()
        );
        CustomPageable customPageable=new CustomPageable(0,5,3,10);
        PaginationResponse<TTVehicleDTO> paginationResponse=new PaginationResponse<>(mockVehicles,customPageable);

        //when
        Mockito.when(ttVehicleService.getVehiclesFiltered(anyInt(), anyInt(), anyList(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(paginationResponse);

        PaginationResponse<TTVehicleDTO> result=ttVehicleController.getAllVehicles(0, 5
                , "","", "", "", "", "",Collections.emptyList()
                , "ASC");

        //then
        assertEquals(0,result.getPageable().getPageNumber());
        assertEquals(5,result.getPageable().getPageSize());
        assertEquals(3,result.getPageable().getTotalPages());
        assertEquals(10,result.getPageable().getTotalElements());
    }

    @Test
    void addVehicle_Success() {
        //given
        TTVehicleDTO ttVehicleDTO=new TTVehicleDTO(1L,"Supra","001", LocalDate.of(2000,3,3),
                EngineType.DIESEL, TransmissionType.MANUAL,"Red",Collections.emptyList());

        //when
        Mockito.when(ttVehicleService.addVehicle(any(TTVehicleDTO.class))).thenReturn(ttVehicleDTO);

        ResponseEntity<TTVehicleDTO> result =ttVehicleController.addVehicle(ttVehicleDTO);
        //then
        Mockito.verify(ttVehicleService).addVehicle(any(TTVehicleDTO.class));
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        TTVehicleDTO resultBody=result.getBody();
        assertNotNull(resultBody);
        assertEquals(ttVehicleDTO.getId(), resultBody.getId());
        assertEquals(ttVehicleDTO.getModel(), resultBody.getModel());
        assertEquals(ttVehicleDTO.getVin(), resultBody.getVin());
        assertEquals(ttVehicleDTO.getEngineType(), resultBody.getEngineType());
        assertEquals(ttVehicleDTO.getTransmissionType(), resultBody.getTransmissionType());
    }
    @Test
    void addVehicle_BadRequest() {
        //given
        TTVehicleDTO ttVehicleDTO=new TTVehicleDTO();

        //when
        Mockito.when(ttVehicleService.addVehicle(any(TTVehicleDTO.class))).thenReturn(null);

        ResponseEntity<TTVehicleDTO> result =ttVehicleController.addVehicle(ttVehicleDTO);
        //then
        Mockito.verify(ttVehicleService).addVehicle(any(TTVehicleDTO.class));
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void updateVehicle_Success() {
        //given
        TTVehicleDTO ttVehicleDTO=new TTVehicleDTO();
        Long vehicleId=1L;
        //when
        Mockito.when(ttVehicleService.updateVehicle(anyLong(),any(TTVehicleDTO.class))).thenReturn(ttVehicleDTO);

        ResponseEntity<TTVehicleDTO> result =ttVehicleController.updateVehicle(vehicleId,ttVehicleDTO);

        //then
        Mockito.verify(ttVehicleService).updateVehicle(anyLong(),any(TTVehicleDTO.class));
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }
    @Test
    void updateVehicle_BadRequest() {
        //given
        TTVehicleDTO ttVehicleDTO=new TTVehicleDTO();
        Long vehicleId=1L;
        //when
        Mockito.when(ttVehicleService.updateVehicle(anyLong(),any(TTVehicleDTO.class))).thenReturn(null);

        ResponseEntity<TTVehicleDTO> result =ttVehicleController.updateVehicle(vehicleId,ttVehicleDTO);

        //then
        Mockito.verify(ttVehicleService).updateVehicle(anyLong(),any(TTVehicleDTO.class));
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void deleteVehicle_Success() {
        TTVehicleDTO ttVehicleDTO=new TTVehicleDTO();
        Long vehicleId=1L;
        //when
        Mockito.when(ttVehicleService.deleteVehicle(anyLong())).thenReturn(ttVehicleDTO);

        ResponseEntity<TTVehicleDTO> result =ttVehicleController.deleteVehicle(vehicleId);

        //then
        Mockito.verify(ttVehicleService).deleteVehicle(anyLong());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    @Test
    void deleteVehicle_NotFound() {
        Long vehicleId=1L;
        //when
        Mockito.when(ttVehicleService.deleteVehicle(anyLong())).thenReturn(null);

        ResponseEntity<TTVehicleDTO> result =ttVehicleController.deleteVehicle(vehicleId);

        //then
        Mockito.verify(ttVehicleService).deleteVehicle(anyLong());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}