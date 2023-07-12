package com.toyota.errorloginservice.service.impl;


import com.toyota.errorloginservice.dao.TTVehicleDefectLocationRepository;
import com.toyota.errorloginservice.dao.TTVehicleDefectRepository;
import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorloginservice.exception.EntityNotFoundException;
import com.toyota.errorloginservice.exception.ImageNotFoundException;
import com.toyota.errorloginservice.exception.ImageProcessingException;
import com.toyota.errorloginservice.exception.InvalidLocationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
@ExtendWith(MockitoExtension.class)
class TTVehicleDefectLocationServiceImplTest {


    private TTVehicleDefectLocationServiceImpl defectLocationService;
    @Mock
    private TTVehicleDefectLocationRepository locationRepository;
    @Mock
    private TTVehicleDefectRepository defectRepository;

    private MockedStatic<ImageIO> mockedStatic;
    @BeforeEach
    void setUp() {
        mockedStatic=Mockito.mockStatic(ImageIO.class);
        defectLocationService=new TTVehicleDefectLocationServiceImpl(locationRepository,defectRepository);
    }
    @AfterEach
    void cleanup() {
        mockedStatic.close();
    }
    @Test
    void add() throws IOException {

        //given
        TTVehicleDefect existingDefect=new TTVehicleDefect();
        existingDefect.setDefectImage(new byte[]{1});
        BufferedImage bufferedImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        int xAxis=150;
        int yAxis=200;
        int height=20;
        int width=20;
        String color="FF0F00";
        TTVehicleDefectLocationDTO locationDTO=new TTVehicleDefectLocationDTO(1L,xAxis,yAxis,width,height,color);
        //when
        Mockito.when(ImageIO.read(any(ByteArrayInputStream.class))).thenReturn(bufferedImage);
        Mockito.when(defectRepository.findById(any())).thenReturn(Optional.of(existingDefect));
        Mockito.when(locationRepository.save(any(TTVehicleDefectLocation.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        defectLocationService.add(1L,locationDTO);

        //then
        Mockito.verify(locationRepository).save(any(TTVehicleDefectLocation.class));
        assertNotNull(existingDefect.getLocation());
        assertEquals(1,existingDefect.getLocation().size());
        assertEquals(xAxis,existingDefect.getLocation().get(0).getX_Axis());
        assertEquals(yAxis,existingDefect.getLocation().get(0).getY_Axis());
    }

    @Test
    void add_ImageNotFound() {

        //given
        TTVehicleDefect existingDefect=new TTVehicleDefect();
        TTVehicleDefectLocationDTO locationDTO=new TTVehicleDefectLocationDTO();
        //when
        Mockito.when(defectRepository.findById(any())).thenReturn(Optional.of(existingDefect));

        //then
        assertThrows(ImageNotFoundException.class,()->defectLocationService.add(1L,locationDTO));
    }
    @Test
    void add_DefectNotFound()  {

        //given
        TTVehicleDefectLocationDTO locationDTO=new TTVehicleDefectLocationDTO();
        //when
        Mockito.when(defectRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class,()->defectLocationService.add(1L,locationDTO));
    }

    @Test
    void add_LocationX_NotValid() throws IOException {

        //given
        TTVehicleDefect existingDefect=new TTVehicleDefect();
        existingDefect.setDefectImage(new byte[]{1});
        BufferedImage bufferedImage = new BufferedImage(100, 300, BufferedImage.TYPE_INT_RGB);
        int xAxis=150;
        int yAxis=200;
        int height=20;
        int width=20;
        String color="FF0F00";
        TTVehicleDefectLocationDTO locationDTO=new TTVehicleDefectLocationDTO(1L,xAxis,yAxis,width,height,color);
        //when
        Mockito.when(ImageIO.read(any(ByteArrayInputStream.class))).thenReturn(bufferedImage);
        Mockito.when(defectRepository.findById(any())).thenReturn(Optional.of(existingDefect));

        //then
        assertThrows(InvalidLocationException.class,()->defectLocationService.add(1L,locationDTO));
    }

    @Test
    void add_LocationY_NotValid() throws IOException {

        //given
        TTVehicleDefect existingDefect=new TTVehicleDefect();
        existingDefect.setDefectImage(new byte[]{1});
        BufferedImage bufferedImage = new BufferedImage(300, 100, BufferedImage.TYPE_INT_RGB);
        int xAxis=150;
        int yAxis=200;
        int height=20;
        int width=20;
        String color="FF0F00";
        TTVehicleDefectLocationDTO locationDTO=new TTVehicleDefectLocationDTO(1L,xAxis,yAxis,width,height,color);
        //when
        Mockito.when(ImageIO.read(any(ByteArrayInputStream.class))).thenReturn(bufferedImage);
        Mockito.when(defectRepository.findById(any())).thenReturn(Optional.of(existingDefect));

        //then
        assertThrows(InvalidLocationException.class,()->defectLocationService.add(1L,locationDTO));
    }

    @Test
    void add_ImageProcessingException() throws IOException {

        //given
        TTVehicleDefect existingDefect=new TTVehicleDefect();
        existingDefect.setDefectImage(new byte[]{1});
        int xAxis=150;
        int yAxis=200;
        int height=20;
        int width=20;
        String color="FF0F00";
        TTVehicleDefectLocationDTO locationDTO=new TTVehicleDefectLocationDTO(1L,xAxis,yAxis,width,height,color);
        //when
        Mockito.when(ImageIO.read(any(ByteArrayInputStream.class))).thenThrow(new IOException());
        Mockito.when(defectRepository.findById(any())).thenReturn(Optional.of(existingDefect));
        //then
        assertThrows(ImageProcessingException.class,()->defectLocationService.add(1L,locationDTO));
    }
    @Test
    void delete() {
        //given
        TTVehicleDefect existingDefect=new TTVehicleDefect();
        existingDefect.setId(22L);
        int xAxis=150;
        int yAxis=200;
        int height=20;
        int width=20;
        String color="FF0F00";
        TTVehicleDefectLocation location=new TTVehicleDefectLocation(1L,xAxis,yAxis,height,width,color,
                false,existingDefect);

        //when
        Mockito.when(locationRepository.findById(any())).thenReturn(Optional.of(location));
        Mockito.when(locationRepository.save(any(TTVehicleDefectLocation.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        defectLocationService.delete(1L);

        //then
        Mockito.verify(locationRepository).save(any(TTVehicleDefectLocation.class));
        assertTrue(location.isDeleted());
    }
    @Test
    void delete_LocationNotFound() {
        //given
        //when
        Mockito.when(locationRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class,()->defectLocationService.delete(1L));
    }

    @Test
    void update() throws IOException {
        //given
        TTVehicleDefect existingDefect=new TTVehicleDefect();
        existingDefect.setDefectImage(new byte[]{1});
        BufferedImage bufferedImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        int xAxis=150;
        int yAxis=200;
        int height=20;
        int width=20;
        String color="FF0F00";
        TTVehicleDefectLocation location=new TTVehicleDefectLocation(1L,xAxis,yAxis,height,width,color,
                false,existingDefect);
        TTVehicleDefectLocationDTO locationDTO=new TTVehicleDefectLocationDTO(1L,100,120,
                width,height,color);
        //when
        Mockito.when(ImageIO.read(any(ByteArrayInputStream.class))).thenReturn(bufferedImage);
        Mockito.when(locationRepository.findById(any())).thenReturn(Optional.of(location));
        defectLocationService.update(1L,locationDTO);
        //then
        assertEquals(locationDTO.getX_Axis(),locationDTO.getX_Axis());
        assertEquals(locationDTO.getY_Axis(),locationDTO.getY_Axis());
    }

    @Test
    void update_LocationNotFound()  {
        //given
        TTVehicleDefectLocationDTO locationDTO=new TTVehicleDefectLocationDTO();
        //when
        Mockito.when(locationRepository.findById(any())).thenReturn(Optional.empty());
        //then
        assertThrows(EntityNotFoundException.class,()->defectLocationService.update(1L,locationDTO));
    }

}