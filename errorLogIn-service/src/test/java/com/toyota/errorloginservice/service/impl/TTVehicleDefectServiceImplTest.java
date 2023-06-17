package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleDefectRepository;
import com.toyota.errorloginservice.dao.TTVehicleRepository;
import com.toyota.errorloginservice.domain.*;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TTVehicleDefectServiceImplTest {

    private TTVehicleDefectServiceImpl ttVehicleDefectService;
    private ModelMapper modelMapper;
    @Mock
    private TTVehicleDefectRepository defectRepository;
    @Mock
    private TTVehicleRepository vehicleRepository;
    @BeforeEach
    void setUp() {
        modelMapper=new ModelMapper();
        ttVehicleDefectService=new TTVehicleDefectServiceImpl(defectRepository,vehicleRepository,modelMapper);
    }
    @Test
    void getAllFiltered() {
    }

    @Test
    void addDefect() {
        //given
        List<TTVehicleDefectLocationDTO> locations= List.of(new TTVehicleDefectLocationDTO(1L,15,30));
        TTVehicleDefectDTO defectDTO=new TTVehicleDefectDTO(1L,"Broken Window","Windshield",
                State.MAJOR,null,locations);
        TTVehicle vehicle = new TTVehicle(1L, "Supra", "0001", LocalDate.now(), EngineType.DIESEL,
                TransmissionType.MANUAL, "Red",false,new ArrayList<>());
        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);

        //when
        when(vehicleRepository.findById(any())).thenReturn(Optional.of(vehicle));
        when(defectRepository.save(any(TTVehicleDefect.class))).thenAnswer(
                invocation -> invocation.getArgument(0)
        );
        TTVehicleDefectDTO result=ttVehicleDefectService.addDefect(request,1L,defectDTO);

        //then
        Mockito.verify(defectRepository).save(any(TTVehicleDefect.class));
        assertNotNull(vehicle.getDefect());
        assertEquals(defectDTO.getType(),result.getType());
        assertEquals(defectDTO.getDescription(),result.getDescription());
        assertEquals(defectDTO.getState(),result.getState());
        assertEquals(defectDTO.getReportTime(),result.getReportTime());
        assertNotNull(result.getLocation());
    }

    @Test
    void update() {
        //given
        TTVehicleDefect defect=new TTVehicleDefect();
        defect.setId(1L);
        defect.setType("Broken Window");
        defect.setDescription("Windshield");
        defect.setState(State.MAJOR);

        TTVehicleDefectDTO updatedDefect=new TTVehicleDefectDTO();
        updatedDefect.setId(1L);
        updatedDefect.setType("Flat Tire");
        updatedDefect.setDescription("In front right");
        updatedDefect.setState(State.MINOR);

        //when
        when(defectRepository.findById(any())).thenReturn(Optional.of(defect));
        when(defectRepository.save(any(TTVehicleDefect.class))).thenAnswer(
                invocation -> invocation.getArgument(0)
        );
        ttVehicleDefectService.update(1L,updatedDefect);
        //then
        verify(defectRepository).save(any(TTVehicleDefect.class));
        assertEquals(updatedDefect.getId(),defect.getId());
        assertEquals(updatedDefect.getType(),defect.getType());
        assertEquals(updatedDefect.getDescription(),defect.getDescription());
        assertEquals(updatedDefect.getState(),defect.getState());




    }

    @Test
    void deleteDefect() {

        //given
        TTVehicleDefect defect=new TTVehicleDefect(1L,"Broken Window","Windshield",State.MAJOR,LocalDateTime.now()
                ,"User",null,false,null,null);
        List<TTVehicleDefectLocation> locations=List.of(
                new TTVehicleDefectLocation(1L,30,40,false,defect),
                new TTVehicleDefectLocation(2L,40,50,false,defect)
        );
        defect.setLocation(locations);

        //when
        when(defectRepository.findById(any())).thenReturn(Optional.of(defect));
        when(defectRepository.save(any(TTVehicleDefect.class))).thenAnswer(
                invocation -> invocation.getArgument(0)
        );
        ttVehicleDefectService.deleteDefect(1L);

        //then
        verify(defectRepository).save(any(TTVehicleDefect.class));
        assertEquals(true,defect.isDeleted());
        assertEquals(true,locations.get(0).isDeleted());
        assertEquals(true,locations.get(1).isDeleted());
    }
}