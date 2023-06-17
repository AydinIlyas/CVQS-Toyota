package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleRepository;
import com.toyota.errorloginservice.domain.EngineType;
import com.toyota.errorloginservice.domain.TTVehicle;
import com.toyota.errorloginservice.domain.TransmissionType;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TTVehicleServiceImplTest {
    @Mock
    private TTVehicleRepository ttVehicleRepository;
    ModelMapper modelMapper;

    private TTVehicleServiceImpl ttVehicleService;


    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        ttVehicleService = new TTVehicleServiceImpl(ttVehicleRepository, modelMapper);
    }
    @Test
    void getVehiclesFiltered() {
//        //given
//        List<TTVehicle> mockVehicles=List.of(
//        new TTVehicle(1L,"SUPRA","12",LocalDate.now(),EngineType.DIESEL,
//                TransmissionType.MANUAL,"red",false,null),
//        new TTVehicle(2L,"Yaris","123",LocalDate.now(),EngineType.DIESEL,
//                TransmissionType.AUTOMATIC,"yellow",false,null),
//        new TTVehicle(3L,"Aygo","124",LocalDate.now(),EngineType.DIESEL,
//                TransmissionType.MANUAL,"red",true,null),
//        new TTVehicle(4L,"Corolla","125",LocalDate.now(),EngineType.GASOLINE,
//                TransmissionType.AUTOMATIC,"Orange",false,null));
//        Sort.Order sort=new Sort.Order(Sort.Direction.ASC,"model");
//        Pageable pageable= PageRequest.of(0,3,Sort.by(sort));
//
//        //when
//        Page<TTVehicle> pageMock=new PageImpl<>(mockVehicles);
//        when(ttVehicleRepository.getVehiclesFiltered(any(),any(),any(),any()
//                ,any(),any(),pageable)).thenAnswer(invocation -> pageMock);
//        List<String> sortBy=new ArrayList<>();
//        sortBy.add("");
//        PaginationResponse<TTVehicleDTO> page=ttVehicleService.getVehiclesFiltered(0,3,sortBy,"ASC"
//                ,"","","","","","");
//        String a="12";
    }

    @Test
    void addVehicle() {

        //given
        TTVehicleDTO vehicleDTO = new TTVehicleDTO(1L, "Supra", "0001", LocalDate.now(), EngineType.DIESEL,
                TransmissionType.MANUAL, "Red", new ArrayList<>());
        //when
        when(ttVehicleRepository.save(Mockito.any(TTVehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));
        TTVehicleDTO result=ttVehicleService.addVehicle(vehicleDTO);

        //then
        assertEquals(vehicleDTO.getId(), result.getId());
        assertEquals(vehicleDTO.getVin(),result.getVin());
        assertEquals(vehicleDTO.getModel(),result.getModel());
        assertEquals(vehicleDTO.getYearOfProduction(),result.getYearOfProduction());
        assertEquals(vehicleDTO.getEngineType(),result.getEngineType());
        assertEquals(vehicleDTO.getTransmissionType(),result.getTransmissionType());
        assertEquals(vehicleDTO.getColor(),result.getColor());
    }

    @Test
    void updateVehicle() {
        // given
        long vehicleId = 1L;
        TTVehicleDTO updatedVehicleDTO = new TTVehicleDTO(vehicleId, "Updated Supra", "0002",
                LocalDate.of(2003,03,31), EngineType.DIESEL,
                TransmissionType.MANUAL, "Blue", new ArrayList<>());

        TTVehicle existingVehicle=new TTVehicle(vehicleId, "Supra", "0001",
                LocalDate.now(), EngineType.DIESEL,
                TransmissionType.MANUAL, "Red",false,new ArrayList<>());

        //when
        when(ttVehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(ttVehicleRepository.save(Mockito.any(TTVehicle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TTVehicleDTO result = ttVehicleService.updateVehicle(vehicleId, updatedVehicleDTO);

        // then
        assertEquals(updatedVehicleDTO.getId(), result.getId());
        assertEquals(updatedVehicleDTO.getVin(),result.getVin());
        assertEquals(updatedVehicleDTO.getModel(),result.getModel());
        assertEquals(updatedVehicleDTO.getYearOfProduction(),result.getYearOfProduction());
        assertEquals(updatedVehicleDTO.getEngineType(),result.getEngineType());
        assertEquals(updatedVehicleDTO.getTransmissionType(),result.getTransmissionType());
        assertEquals(updatedVehicleDTO.getColor(),result.getColor());
    }

    @Test
    void deleteVehicle() {
        //given
        Long vehicleId=1L;
        TTVehicle existingVehicle=new TTVehicle(vehicleId, "Supra", "0001",
                LocalDate.now(), EngineType.DIESEL,
                TransmissionType.MANUAL, "Red",false,new ArrayList<>());

        //when
        when(ttVehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        ttVehicleService.deleteVehicle(vehicleId);

        //then
        assertEquals(true,existingVehicle.isDeleted());
    }
}