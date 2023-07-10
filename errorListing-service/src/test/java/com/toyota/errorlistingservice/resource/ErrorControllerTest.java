package com.toyota.errorlistingservice.resource;

import com.toyota.errorlistingservice.dto.CustomPageable;
import com.toyota.errorlistingservice.dto.PaginationResponse;
import com.toyota.errorlistingservice.service.impl.ErrorListingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ErrorControllerTest {
    @Mock
    private ErrorListingServiceImpl errorListingService;
    @InjectMocks
    private ErrorController errorController;

    @Test
    void getAllVehiclesFiltered() {
        //given
        HttpServletRequest request= Mockito.mock(HttpServletRequest.class);
        int page=0;
        int size=3;
        List<String> sortBy=List.of("model");
        String sortOrder="Asc";
        String model="model";
        String vin="vin";
        String yearOfProduction="yearOfProd";
        String transmissionType="transmissionType";
        String engineType="engineType";
        String color="color";
        List<Object> content= Collections.emptyList();
        CustomPageable pageable=new CustomPageable(page,size,2,3);
        PaginationResponse<Object> pageMock=new PaginationResponse<>(content,pageable);
        Mono<PaginationResponse<Object>> monoPage=Mono.just(pageMock);
        //when
        when(errorListingService.getVehicles(request,page,size,sortBy,sortOrder,model
                ,vin,yearOfProduction,transmissionType,engineType,color)).thenReturn(monoPage);
        Mono<PaginationResponse<Object>> responseMono=errorController.getAllVehiclesFiltered(request,page,size,sortBy,sortOrder,model
                ,vin,yearOfProduction,transmissionType,engineType,color);

        //then
        StepVerifier.create(responseMono)
                .expectNextMatches(response-> {
                    assertNotNull(response);
                    assertEquals(pageMock,response);
                    return true;
                })
                .verifyComplete();


    }

    @Test
    void getAllDefectsFiltered() {
        //given
        HttpServletRequest request= Mockito.mock(HttpServletRequest.class);
        int page=0;
        int size=3;
        String sortBy="type";
        String sortOrder="Asc";
        String type="type";
        String state="state";
        String reportTime="reportTime";
        String reportedBy="reportedBy";
        String vin="vin";
        List<Object> content= Collections.emptyList();
        CustomPageable pageable=new CustomPageable(page,size,2,3);
        PaginationResponse<Object> pageMock=new PaginationResponse<>(content,pageable);
        Mono<PaginationResponse<Object>> monoPage=Mono.just(pageMock);
        //when
        when(errorListingService.getDefects(request,page,size,type
                ,state,reportTime,reportedBy,vin,sortOrder,sortBy)).thenReturn(monoPage);
        Mono<PaginationResponse<Object>> responseMono=errorController.getAllDefectsFiltered(request,page,size,type
                ,state,reportTime,reportedBy,vin,sortOrder,sortBy);

        //then
        StepVerifier.create(responseMono)
                .expectNextMatches(response-> {
                    assertNotNull(response);
                    assertEquals(pageMock,response);
                    return true;
                })
                .verifyComplete();
    }
}