package com.toyota.errorloginservice.service.impl;


import com.toyota.errorloginservice.dao.TTVehicleDefectLocationRepository;
import com.toyota.errorloginservice.dao.TTVehicleDefectRepository;
import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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


    @BeforeEach
    void setUp() {
        defectLocationService=new TTVehicleDefectLocationServiceImpl(locationRepository,defectRepository);
    }
    @Test
    void add() throws IOException {

        //given
        TTVehicleDefect existingDefect=new TTVehicleDefect();
        existingDefect.setDefectImage(new byte[]{1});
        BufferedImage bufferedImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        int xAxis=150;
        int yAxis=200;
        TTVehicleDefectLocationDTO locationDTO=new TTVehicleDefectLocationDTO(1L,xAxis,yAxis);
        //when
        Mockito.mockStatic(ImageIO.class);
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
    void delete() {
        //given
        TTVehicleDefect existingDefect=new TTVehicleDefect();
        existingDefect.setId(22L);
        int xAxis=150;
        int yAxis=200;
        TTVehicleDefectLocation location=new TTVehicleDefectLocation(1L,xAxis,yAxis,false,existingDefect);

        //when
        Mockito.when(locationRepository.findById(any())).thenReturn(Optional.of(location));
        Mockito.when(locationRepository.save(any(TTVehicleDefectLocation.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        defectLocationService.delete(1L);

        //then
        Mockito.verify(locationRepository).save(any(TTVehicleDefectLocation.class));
        assertTrue(location.isDeleted());
    }
}