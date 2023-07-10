package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleDefectRepository;
import com.toyota.errorloginservice.dao.TTVehicleRepository;
import com.toyota.errorloginservice.domain.*;
import com.toyota.errorloginservice.dto.ImageDTO;
import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorloginservice.exception.EntityNotFoundException;
import com.toyota.errorloginservice.exception.ImageNotFoundException;
import com.toyota.errorloginservice.exception.ImageProcessingException;
import com.toyota.errorloginservice.service.common.MapUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Mock
    private TTVehicleDefectRepository defectRepository;
    @Mock
    private TTVehicleRepository vehicleRepository;
    @BeforeEach
    void setUp() {
        MapUtil mapUtil = new MapUtil(new ModelMapper());
        ttVehicleDefectService=new TTVehicleDefectServiceImpl(defectRepository,vehicleRepository, mapUtil);
    }
    @Test
    void getAllFiltered() {
        //given
        String type="Flat Tire";
        String state=State.MAJOR.toString();
        String reportTime="2002-01-01";
        String reportedBy=EngineType.DIESEL.toString();
        String vin="01";
        Sort.Order sort=new Sort.Order(Sort.Direction.ASC,"type");
        Pageable pageable= PageRequest.of(0,3,Sort.by(sort));

        List<TTVehicleDefect> mockDefects=List.of(
                new TTVehicleDefect());
        Page<TTVehicleDefect> pageMock=new PageImpl<>(mockDefects,pageable,1);

        //when
        when(defectRepository.getDefectsFiltered(type,state,reportTime,reportedBy,vin,pageable)).thenReturn(pageMock);

        PaginationResponse<TTVehicleDefectDTO> result=ttVehicleDefectService.getAllFiltered(0,3,type,state,
                reportTime,reportedBy,vin,"ASC", List.of("type"));

        //then
        assertNotNull(result);
        assertEquals(mockDefects.size(),result.getContent().size());
    }

    @Test
    void addDefect_Success() {
        //given
        List<TTVehicleDefectLocationDTO> locations= List.of(new TTVehicleDefectLocationDTO(1L,15,30));
        TTVehicleDefectDTO defectDTO=new TTVehicleDefectDTO(1L,"Broken Window","Windshield",
                State.MAJOR,locations);

        TTVehicle vehicle = new TTVehicle(1L, "Supra", "0001", LocalDate
                .of(2000, 3,30),
                EngineType.DIESEL,
                TransmissionType.MANUAL, "Red",false,new ArrayList<>());
        String username="username";

        //when
        when(vehicleRepository.findById(any())).thenReturn(Optional.of(vehicle));
        when(defectRepository.save(any(TTVehicleDefect.class))).thenAnswer(
                invocation -> invocation.getArgument(0)
        );
        TTVehicleDefectDTO result=ttVehicleDefectService.addDefect(username,1L,defectDTO);

        //then
        Mockito.verify(defectRepository).save(any(TTVehicleDefect.class));
        assertNotNull(vehicle.getDefect());
        assertEquals(defectDTO.getType(),result.getType());
        assertEquals(defectDTO.getDescription(),result.getDescription());
        assertEquals(defectDTO.getState(),result.getState());
        assertNotNull(result.getLocation());
    }
    @Test
    void addDefect_Fail() {
        //given
        TTVehicleDefectDTO defectDTO=new TTVehicleDefectDTO();
        String username="username";

        //when
        when(vehicleRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()->ttVehicleDefectService.addDefect(username,1L,defectDTO));
    }

    @Test
    void update_Success() {
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
    void update_Fail() {
        //given
        TTVehicleDefectDTO updatedDefect=new TTVehicleDefectDTO();

        //when
        when(defectRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->ttVehicleDefectService.update(1L,updatedDefect));




    }

    @Test
    void deleteDefect_Success() {

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
        assertTrue(defect.isDeleted());
        assertTrue(locations.get(0).isDeleted());
        assertTrue(locations.get(1).isDeleted());
    }

    @Test
    void deleteDefect_Fail() {

        //given
        Long defectId=1L;
        //when
        when(defectRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class,()->ttVehicleDefectService.deleteDefect(defectId));
    }

    @Test
    void addImage() throws IOException {
        //given
        TTVehicleDefect defect=new TTVehicleDefect(1L,"Broken Window","Windshield",State.MAJOR,LocalDateTime.now()
                ,"User",null,false,null,new ArrayList<>());
        MultipartFile imageMock=Mockito.mock(MultipartFile.class);
        Long defectId=1L;
        //when
        when(defectRepository.findById(any())).thenReturn(Optional.of(defect));
        when(defectRepository.save(any(TTVehicleDefect.class))).thenAnswer(
                invocation -> invocation.getArgument(0)
        );
        when(imageMock.getBytes()).thenReturn(new byte[1]);
        ttVehicleDefectService.addImage(defectId,imageMock);

        //then
        assertNotNull(defect.getDefectImage());
    }

    @Test
    void addImage_ImageNotFound() throws IOException {
        //given
        TTVehicleDefect defect=new TTVehicleDefect(1L,"Broken Window","Windshield",State.MAJOR,LocalDateTime.now()
                ,"User",null,false,null,new ArrayList<>());
        MultipartFile imageMock=Mockito.mock(MultipartFile.class);
        Long defectId=1L;
        //when
        when(defectRepository.findById(any())).thenReturn(Optional.of(defect));
        when(imageMock.getBytes()).thenReturn(new byte[0]);

        //then
        assertThrows(ImageNotFoundException.class,()->ttVehicleDefectService.addImage(defectId,imageMock));
    }

    @Test
    void addImage_DefectNotFound()  {
        //given
        MultipartFile imageMock=Mockito.mock(MultipartFile.class);
        Long defectId=1L;
        //when
        when(defectRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class,()->ttVehicleDefectService.addImage(defectId,imageMock));
    }

    @Test
    void addImage_IOException() throws IOException {
        //given
        TTVehicleDefect defect=new TTVehicleDefect(1L,"Broken Window","Windshield",State.MAJOR,LocalDateTime.now()
                ,"User",null,false,null,new ArrayList<>());
        MultipartFile imageMock=Mockito.mock(MultipartFile.class);
        Long defectId=1L;
        //when
        when(defectRepository.findById(any())).thenReturn(Optional.of(defect));
        when(imageMock.getBytes()).thenThrow(new IOException());

        //then
        assertThrows(ImageProcessingException.class,()->ttVehicleDefectService.addImage(defectId,imageMock));
    }
    @Test
    void getImage_Success()
    {
        //given
        TTVehicleDefect defect=new TTVehicleDefect(1L,"Broken Window","Windshield",State.MAJOR,LocalDateTime.now()
                ,"User",new byte[2],false,null,List.of(new TTVehicleDefectLocation()));
        Long defectId=1L;
        //when
        when(defectRepository.findById(any())).thenReturn(Optional.of(defect));
        ImageDTO result=ttVehicleDefectService.getImage(defectId);

        //then
        assertEquals(defect.getDefectImage(),result.getImage());
        assertEquals(defect.getLocation().size(),result.getLocationDTO().size());
    }

    @Test
    void getImage_DefectNotFound()
    {
        //given
        Long defectId=1L;
        //when
        when(defectRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class,()->ttVehicleDefectService.getImage(defectId));
    }

    @Test
    void getImage_ImageNotFound()
    {
        //given
        TTVehicleDefect defect=new TTVehicleDefect(1L,"Broken Window","Windshield",State.MAJOR,LocalDateTime.now()
                ,"User",null,false,null,List.of(new TTVehicleDefectLocation()));
        Long defectId=1L;
        //when
        when(defectRepository.findById(any())).thenReturn(Optional.of(defect));
        ImageDTO imageDTO=ttVehicleDefectService.getImage(defectId);
        //then
        assertNull(imageDTO);
    }

    @Test
    void removeImage_Success()
    {
        //given
        TTVehicleDefect defect=new TTVehicleDefect(1L,"Broken Window","Windshield",State.MAJOR,LocalDateTime.now()
                ,"User",new byte[2],false,null,List.of(new TTVehicleDefectLocation()));
        Long defectId=1L;
        //when
        when(defectRepository.findById(any())).thenReturn(Optional.of(defect));
        ttVehicleDefectService.removeImage(defectId);

        //then
        assertNull(defect.getDefectImage());
    }
    @Test
    void removeImage_DefectNotFound()
    {
        //given
        Long defectId=1L;
        //when
        when(defectRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class,()->ttVehicleDefectService.removeImage(defectId));
    }
}