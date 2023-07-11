package com.toyota.errorloginservice.advice;

import com.toyota.errorloginservice.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler=new GlobalExceptionHandler();
    }

    @Test
    void handleEntityNotFound() {
        //given
        String message="Entity not found";
        EntityNotFoundException entityNotFoundException=new EntityNotFoundException(message);

        //when
        String path="/test";
        HttpServletRequest request= mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleEntityNotFound
                (entityNotFoundException,request);
        //then
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),errorResponse.getStatus());
        Assertions.assertEquals(message,errorResponse.getMessage());
        Assertions.assertEquals(path,errorResponse.getPath());
    }

    @Test
    void handleVehicleAlreadyExistsException() {
        //given
        String message="Vehicle Already exists";
        VehicleAlreadyExistsException vehicleAlreadyExistsException=new VehicleAlreadyExistsException(message);

        //when
        String path="/test";
        HttpServletRequest request= mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleVehicleAlreadyExistsException
                (vehicleAlreadyExistsException,request);
        //then
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.CONFLICT.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.CONFLICT.value(),errorResponse.getStatus());
        Assertions.assertEquals(message,errorResponse.getMessage());
        Assertions.assertEquals(path,errorResponse.getPath());
    }

    @Test
    void handleImageProcessingException() {
        //given
        String message="Image processing exception";
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

    @Test
    void handleImageNotFoundException() {
        //given
        String message="Image not found";
        ImageNotFoundException imageNotFoundException=new ImageNotFoundException(message);

        //when
        String path="/test";
        HttpServletRequest request= mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleImageNotFoundException
                (imageNotFoundException,request);
        //then
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),errorResponse.getStatus());
        Assertions.assertEquals(message,errorResponse.getMessage());
        Assertions.assertEquals(path,errorResponse.getPath());

    }

    @Test
    void handleInvalidLocationException() {
        //given
        String message="Invalid Location";
        InvalidLocationException invalidLocationException=new InvalidLocationException(message);

        //when
        String path="/test";
        HttpServletRequest request= mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleInvalidLocationException
                (invalidLocationException,request);
        //then
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),errorResponse.getStatus());
        Assertions.assertEquals(message,errorResponse.getMessage());
        Assertions.assertEquals(path,errorResponse.getPath());
    }

    @Test
    void handleMethodArgumentNotValid() {
        //given
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        HttpHeaders headers = new HttpHeaders();
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("vehicle", "model", "error");
        ServletWebRequest servletWebRequest=mock(ServletWebRequest.class);
        HttpServletRequest httpServletRequest=mock(HttpServletRequest.class);
        String path="/test";

        //when
        Mockito.when(servletWebRequest.getRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getRequestURI()).thenReturn(path);
        Mockito.when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        Mockito.when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Object> response=globalExceptionHandler.handleMethodArgumentNotValid
                (methodArgumentNotValidException,headers,HttpStatus.BAD_REQUEST,servletWebRequest);
        //then
        assertNotNull(response);
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),errorResponse.getStatus());
        ValidationError responseFieldErr=errorResponse.getSubErrors().get(0);
        assertEquals(fieldError.getObjectName(),responseFieldErr.getObject());
        assertEquals(fieldError.getField(),responseFieldErr.getField());
        assertEquals(fieldError.getRejectedValue(),responseFieldErr.getRejectedValue());
    }

    @Test
    void handleHttpMessageNotReadable() {
        //given
        HttpMessageNotReadableException messageNotReadableException=mock(HttpMessageNotReadableException.class);
        HttpHeaders headers=new HttpHeaders();
        HttpStatusCode httpStatusCode=HttpStatusCode.valueOf(400);
        ServletWebRequest servletWebRequest=mock(ServletWebRequest.class);
        HttpServletRequest httpServletRequest=mock(HttpServletRequest.class);
        String path="/test";
        //when
        Mockito.when(servletWebRequest.getRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleHttpMessageNotReadable
                (messageNotReadableException,headers,httpStatusCode,servletWebRequest);
        //then
        assertNotNull(response);
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),errorResponse.getStatus());
        Assertions.assertEquals(path,errorResponse.getPath());


    }
}