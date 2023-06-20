package com.toyota.errorlistingservice.service.impl;

import com.toyota.errorlistingservice.dto.CustomPageable;
import com.toyota.errorlistingservice.dto.PaginationResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        HttpServletRequest request= Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");
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
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Token");
        doAnswer(invocation -> {
            Consumer<HttpHeaders> headersConsumer = invocation.getArgument(0);
            headersConsumer.accept(headers);
            return requestHeadersSpec;
        }).when(requestHeadersSpec).headers(any(Consumer.class));
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mono<PaginationResponse<Object>> mono=Mono.just(pageMock);
        Mono<PaginationResponse<Object>> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(monoSpy);

        PaginationResponse<Object> result=errorListingService.getVehicles(request,page,size,sortBy,sortOrder,model
                ,vin,yearOfProduction,transmissionType,engineType,color);

        //then
        assertNotNull(result);
        assertEquals(page,result.getPageable().getPageNumber());
        assertEquals(size,result.getPageable().getPageSize());
        assertEquals(content,result.getContent());
    }

    @Test
    void getDefects() {
        //given
        HttpServletRequest request= Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");
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
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Token");
        doAnswer(invocation -> {
            Consumer<HttpHeaders> headersConsumer = invocation.getArgument(0);
            headersConsumer.accept(headers);
            return requestHeadersSpec;
        }).when(requestHeadersSpec).headers(any(Consumer.class));
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mono<PaginationResponse<Object>> mono=Mono.just(pageMock);
        Mono<PaginationResponse<Object>> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(monoSpy);

        PaginationResponse<Object> result=errorListingService.getDefects(request,page,size,type
                ,state,reportTime,reportedBy,vin,sortOrder,sortBy);

        //then
        assertNotNull(result);
        assertEquals(page,result.getPageable().getPageNumber());
        assertEquals(size,result.getPageable().getPageSize());
        assertEquals(content,result.getContent());
    }
}