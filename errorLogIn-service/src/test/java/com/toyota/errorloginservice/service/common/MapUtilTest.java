package com.toyota.errorloginservice.service.common;

import com.toyota.errorloginservice.domain.*;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapUtilTest {

    private MapUtil mapUtil;

    @BeforeEach
    void setUp()
    {
        mapUtil=new MapUtil(new ModelMapper());
    }
    @Test
    void convertVehicleWithAllToDTO() {
        TTVehicleDefectLocation location=new TTVehicleDefectLocation(1L,150,200,20,20,
                "FF0000",false,null);
        TTVehicleDefect defect=new TTVehicleDefect(1L,"Flat tire","In front left", State.MAJOR,
                LocalDateTime.now(), "User",null,false,null,null);
        TTVehicle ttVehicle=new TTVehicle(1L,"Supra","0001", LocalDate.of(2000,3,20),
                EngineType.DIESEL, TransmissionType.MANUAL,"Blue",false, List.of(defect));
        defect.setTt_vehicle(ttVehicle);
        defect.setLocation(List.of(location));
        location.setTt_vehicle_defect(defect);

        TTVehicleDTO result=mapUtil.convertVehicleWithAllToDTO(ttVehicle);

        assertEquals(TTVehicleDTO.class,result.getClass());
        assertEquals(TTVehicleDefectDTO.class,result.getDefect().get(0).getClass());
        assertEquals(TTVehicleDefectLocationDTO.class,result.getDefect().get(0).getLocation().get(0).getClass());

    }
}