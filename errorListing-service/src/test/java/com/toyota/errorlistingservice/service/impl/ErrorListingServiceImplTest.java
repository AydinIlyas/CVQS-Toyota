package com.toyota.errorlistingservice.service.impl;

import com.toyota.errorlistingservice.dto.CustomPageable;
import com.toyota.errorlistingservice.dto.ImageDTO;
import com.toyota.errorlistingservice.dto.PaginationResponse;
import com.toyota.errorlistingservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorlistingservice.exceptions.ImageProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ErrorListingServiceImplTest {

    @Mock
    private ErrorListingServiceImpl errorListingService;
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @BeforeEach
    void setUp() {
        errorListingService=new ErrorListingServiceImpl(webClientBuilder);
    }
    @Test
    void getVehicles() {
        //given
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
        //when
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec)
                .uri(anyString(),Mockito.any(Function.class));
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mono<PaginationResponse<Object>> mono=Mono.just(pageMock);
        Mono<PaginationResponse<Object>> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(monoSpy);

        Mono<PaginationResponse<Object>> resultMono=errorListingService.getVehicles(page,size,sortBy,sortOrder,model
                ,vin,yearOfProduction,transmissionType,engineType,color);

        //then
        StepVerifier.create(resultMono)
                        .expectNextMatches(result-> {
                            assertNotNull(result);
                            assertEquals(page, result.getPageable().getPageNumber());
                            assertEquals(size, result.getPageable().getPageSize());
                            assertEquals(content, result.getContent());
                            return true;
                                })
                .verifyComplete();
    }

    @Test
    void getDefects() {
        //given
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
        //when
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec)
                .uri(anyString(),Mockito.any(Function.class));
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mono<PaginationResponse<Object>> mono=Mono.just(pageMock);
        Mono<PaginationResponse<Object>> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(monoSpy);

        Mono<PaginationResponse<Object>> resultMono=errorListingService.getDefects(page,size,type
                ,state,reportTime,reportedBy,vin,sortOrder,sortBy);

        //then
        StepVerifier.create(resultMono)
                .expectNextMatches(result-> {
                    assertNotNull(result);
                    assertEquals(page,result.getPageable().getPageNumber());
                    assertEquals(size,result.getPageable().getPageSize());
                    assertEquals(content,result.getContent());
                    return true;
                })
                .verifyComplete();

    }

    @Test
    void getImage() throws IOException {
        //given
        ImageDTO imageDTO=new ImageDTO(new byte[10],List.of(new TTVehicleDefectLocationDTO(1L,100,120),
                new TTVehicleDefectLocationDTO(2L,140,30)));
        //when
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec
                .uri(anyString(),any(Object.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(),any())).thenReturn(responseSpec);
        Mono<ImageDTO> mono=Mono.just(imageDTO);
        Mono<ImageDTO> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(ImageDTO.class))
                .thenReturn(monoSpy);

        MockedStatic<ImageIO> mockedStatic=mockStatic(ImageIO.class);
        BufferedImage buffImageMock=mock(BufferedImage.class);
        when(ImageIO.read(any(ByteArrayInputStream.class))).thenReturn(buffImageMock);
        Graphics2D mockGraphics2D=mock(Graphics2D.class);
        when(buffImageMock.getGraphics()).thenReturn(mockGraphics2D);
        errorListingService.getImage(1L,"png",10,10,"#FF0000",
                true);
        //then
        verify(mockGraphics2D,times(2)).fillRect(anyInt(),anyInt(),anyInt(),anyInt());
        mockedStatic.close();
    }
    @Test
    void getImage_ImageProcessingException() throws IOException {
        //given
        ImageDTO imageDTO=new ImageDTO(new byte[10],List.of(new TTVehicleDefectLocationDTO(1L,100,120),
                new TTVehicleDefectLocationDTO(2L,140,30)));
        //when
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec
                .uri(anyString(),any(Object.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(),any())).thenReturn(responseSpec);
        Mono<ImageDTO> mono=Mono.just(imageDTO);
        Mono<ImageDTO> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(ImageDTO.class))
                .thenReturn(monoSpy);

        MockedStatic<ImageIO> mockedStatic=mockStatic(ImageIO.class);
        when(ImageIO.read(any(ByteArrayInputStream.class))).thenThrow(new IOException());
        //then
        assertThrows(ImageProcessingException.class,()->errorListingService.getImage(
                1L,"png",10,10,"#FF0000", true));
        mockedStatic.close();
    }
}