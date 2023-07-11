package com.toyota.errorlistingservice.advice;

import com.toyota.errorlistingservice.exceptions.BearerTokenNotFoundException;
import com.toyota.errorlistingservice.exceptions.DefectNotFoundException;
import com.toyota.errorlistingservice.exceptions.ImageProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler=new GlobalExceptionHandler();
    }

    @Test
    void handleBearerTokenNotFoundException() {
        //given
        String message="Bearer not found";
        BearerTokenNotFoundException bearerTokenNotFoundException=new BearerTokenNotFoundException(message);

        //when
        String path="/test";
        HttpServletRequest request= mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleBearerTokenNotFoundException
                (bearerTokenNotFoundException,request);
        //then
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(),errorResponse.getStatus());
        Assertions.assertEquals(message,errorResponse.getMessage());
        Assertions.assertEquals(path,errorResponse.getPath());
    }

    @Test
    void handleDefectNotFoundException() {
        //given
        String message="Defect Not Found";
        DefectNotFoundException defectNotFoundException=new DefectNotFoundException(message);

        //when
        String path="/test";
        HttpServletRequest request= mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleDefectNotFoundException
                (defectNotFoundException,request);
        //then
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),errorResponse.getStatus());
        Assertions.assertEquals(message,errorResponse.getMessage());
        Assertions.assertEquals(path,errorResponse.getPath());
    }

    @Test
    void handleImageProcessingException() {
        //given
        String message="Image processing problem";
        ImageProcessingException imageProcessingException=new ImageProcessingException(message);

        //when
        String path="/test";
        HttpServletRequest request= mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleImageProcessingException
                (imageProcessingException,request);
        //then
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),errorResponse.getStatus());
        Assertions.assertEquals(message,errorResponse.getMessage());
        Assertions.assertEquals(path,errorResponse.getPath());
    }
}