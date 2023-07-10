package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleRepository;
import com.toyota.errorloginservice.domain.*;
import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.exception.EntityNotFoundException;
import com.toyota.errorloginservice.exception.VehicleAlreadyExistsException;
import com.toyota.errorloginservice.service.common.MapUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TTVehicleServiceImplTest {
    @Mock
    private TTVehicleRepository ttVehicleRepository;
    private TTVehicleServiceImpl ttVehicleService;


    @BeforeEach
    void setUp() {
         MapUtil mapUtil= new MapUtil(new ModelMapper());
        ttVehicleService = new TTVehicleServiceImpl(ttVehicleRepository, mapUtil);
    }
    @Test
    void getVehiclesFiltered() {
        //given
        String model="Supra";
        String vin="01";
        String yearOfProduction="2002-01-01";
        String transmissionType=TransmissionType.AUTOMATIC.toString();
        String engineType=EngineType.DIESEL.toString();
        String color="red";
        Sort.Order sort=new Sort.Order(Sort.Direction.ASC,"model");
        Pageable pageable= PageRequest.of(0,3,Sort.by(sort));

        List<TTVehicle> mockVehicles=List.of(
                new TTVehicle(1L,"SUPRA","12",LocalDate.now(),EngineType.DIESEL,
                        TransmissionType.MANUAL,"red",false,null));
        Page<TTVehicle> pageMock=new PageImpl<>(mockVehicles,pageable,1);

        //when
        when(ttVehicleRepository.getVehiclesFiltered(model,vin,yearOfProduction,transmissionType,engineType,color,
                pageable)).thenReturn(pageMock);
        PaginationResponse<TTVehicleDTO> result=ttVehicleService.getVehiclesFiltered(0,3, List.of("model"),"ASC"
                ,model,vin,yearOfProduction,transmissionType,engineType,color);

        //then
        assertNotNull(result);
        assertEquals(mockVehicles.size(),result.getContent().size());
    }

    @Test
    void addVehicle_Success() {

        //given
        TTVehicleDTO vehicleDTO = new TTVehicleDTO(1L, "Supra", "0001", LocalDate.now(), EngineType.DIESEL,
                TransmissionType.MANUAL, "Red", new ArrayList<>());
        //when
        when(ttVehicleRepository.existsByVinAndDeletedIsFalse(anyString())).thenReturn(false);
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
    void addVehicle_VehicleAlreadyExists() {

        //given
        TTVehicleDTO vehicleDTO = new TTVehicleDTO(1L, "Supra", "0001", LocalDate.now(), EngineType.DIESEL,
                TransmissionType.MANUAL, "Red", new ArrayList<>());
        //when
        when(ttVehicleRepository.existsByVinAndDeletedIsFalse(anyString())).thenReturn(true);

        //then
        assertThrows(VehicleAlreadyExistsException.class,()->ttVehicleService.addVehicle(vehicleDTO));
    }

    @Test
    void updateVehicle_Success() {
        // given
        long vehicleId = 1L;
        TTVehicleDTO updatedVehicleDTO = new TTVehicleDTO(vehicleId, "Updated Supra", "0002",
                LocalDate.of(2003,3,31), EngineType.DIESEL,
                TransmissionType.MANUAL, "Blue", new ArrayList<>());

        TTVehicle existingVehicle=new TTVehicle(vehicleId, "Supra", "0001",
                LocalDate.now(), EngineType.ELECTRIC,
                TransmissionType.AUTOMATIC, "Red",false,new ArrayList<>());

        //when
        when(ttVehicleRepository.existsByVinAndDeletedIsFalse(anyString())).thenReturn(false);
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
    void updateVehicle_VehicleNotFound() {
        // given
        long vehicleId = 1L;
        TTVehicleDTO updatedVehicleDTO = new TTVehicleDTO(vehicleId, "Updated Supra", "0002",
                LocalDate.of(2003,3,31), EngineType.DIESEL,
                TransmissionType.MANUAL, "Blue", new ArrayList<>());
        //when
        when(ttVehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class,()-> ttVehicleService.updateVehicle(vehicleId,updatedVehicleDTO));
    }
    @Test
    void updateVehicle_VehicleAlreadyExists() {
        // given
        long vehicleId = 1L;
        TTVehicleDTO updatedVehicleDTO = new TTVehicleDTO(vehicleId, "Updated Supra", "0002",
                LocalDate.of(2003,3,31), EngineType.DIESEL,
                TransmissionType.MANUAL, "Blue", new ArrayList<>());

        TTVehicle existingVehicle=new TTVehicle(vehicleId, "Supra", "0001",
                LocalDate.now(), EngineType.ELECTRIC,
                TransmissionType.AUTOMATIC, "Red",false,new ArrayList<>());

        //when
        when(ttVehicleRepository.existsByVinAndDeletedIsFalse(anyString())).thenReturn(true);
        when(ttVehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));

        // then
        assertThrows(VehicleAlreadyExistsException.class,
                ()->ttVehicleService.updateVehicle(vehicleId, updatedVehicleDTO));
    }
    @Test
    void deleteVehicle_Success() {
        //given
        Long vehicleId=1L;
        List<TTVehicleDefectLocation> locations= List.of(new TTVehicleDefectLocation());
        List<TTVehicleDefect> defects= List.of(new TTVehicleDefect());
        defects.get(0).setLocation(locations);
        TTVehicle existingVehicle=new TTVehicle(vehicleId, "Supra", "0001",
                LocalDate.now(), EngineType.DIESEL,
                TransmissionType.MANUAL, "Red",false,defects);

        //when
        when(ttVehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        ttVehicleService.deleteVehicle(vehicleId);

        //then
        assertTrue(existingVehicle.isDeleted());
        assertTrue(existingVehicle.getDefect().get(0).isDeleted());
        assertTrue(existingVehicle.getDefect().get(0).getLocation().get(0).isDeleted());
    }
    @Test
    void deleteVehicle_VehicleNotFound() {
        //given
        Long vehicleId=1L;

        //when
        when(ttVehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());


        //then
        assertThrows(EntityNotFoundException.class,()->ttVehicleService.deleteVehicle(vehicleId));
    }
}