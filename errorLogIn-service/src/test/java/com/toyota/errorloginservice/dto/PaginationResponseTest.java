package com.toyota.errorloginservice.dto;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaginationResponseTest {

    @Test
    void getContent() {
        CustomPageable pageable=new CustomPageable(1,3,5,20);
        List<TTVehicleDefectLocationDTO> content=List.of(new TTVehicleDefectLocationDTO());
        PaginationResponse<TTVehicleDefectLocationDTO> page=new PaginationResponse<>(content
                ,pageable);
        assertEquals(content,page.getContent());

    }

    @Test
    void getPageable() {
        CustomPageable pageable=new CustomPageable(1,3,5,20);
        List<TTVehicleDefectLocationDTO> content=List.of(new TTVehicleDefectLocationDTO());
        PaginationResponse<TTVehicleDefectLocationDTO> page=new PaginationResponse<>(content
                ,pageable);
        assertEquals(pageable,page.getPageable());
    }

    @Test
    void setContent() {
        List<TTVehicleDefectLocationDTO> content=List.of(new TTVehicleDefectLocationDTO());
        PaginationResponse<TTVehicleDefectLocationDTO> page=new PaginationResponse<>();
        page.setContent(content);
        assertEquals(content,page.getContent());
    }

    @Test
    void setPageable() {
        CustomPageable pageable=new CustomPageable(1,3,5,20);
        PaginationResponse<TTVehicleDefectLocationDTO> page=new PaginationResponse<>();
        page.setPageable(pageable);
        assertEquals(pageable,page.getPageable());
    }
}