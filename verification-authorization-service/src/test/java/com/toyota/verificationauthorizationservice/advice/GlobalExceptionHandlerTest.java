package com.toyota.verificationauthorizationservice.advice;

import com.toyota.verificationauthorizationservice.exception.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler=new GlobalExceptionHandler();
    }
    @Test
    void handleUsernameTakenException() {
        //given
        String message="Username taken";
        UsernameTakenException usernameTakenException=new UsernameTakenException(message);

        //when
        String path="/test";
        HttpServletRequest request= mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleUsernameTakenException
                (usernameTakenException,request);
        //then
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.CONFLICT.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.CONFLICT.value(),errorResponse.getStatus());
        Assertions.assertEquals(message,errorResponse.getMessage());
        Assertions.assertEquals(path,errorResponse.getPath());
    }

    @Test
    void handleUserNotFoundException() {
        //given
        String message="User not found";
        UserNotFoundException userNotFoundException=new UserNotFoundException(message);

        //when
        String path="/test";
        HttpServletRequest request= mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleUserNotFoundException
                (userNotFoundException,request);
        //then
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),errorResponse.getStatus());
        Assertions.assertEquals(message,errorResponse.getMessage());
        Assertions.assertEquals(path,errorResponse.getPath());

    }

    @Test
    void handleInvalidBearerToken() {
        //given
        String message="Bearer not found";
        InvalidBearerToken invalidBearerToken=new InvalidBearerToken(message);

        //when
        String path="/test";
        HttpServletRequest request= mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleInvalidBearerToken
                (invalidBearerToken,request);
        //then
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),errorResponse.getStatus());
        Assertions.assertEquals(message,errorResponse.getMessage());
        Assertions.assertEquals(path,errorResponse.getPath());
    }

    @Test
    void handleNoRolesException() {
        //given
        String message="No Roles exception";
        NoRolesException noRolesException=new NoRolesException(message);

        //when
        String path="/test";
        HttpServletRequest request= mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleNoRolesException
                (noRolesException,request);
        //then
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),errorResponse.getStatus());
        Assertions.assertEquals(message,errorResponse.getMessage());
        Assertions.assertEquals(path,errorResponse.getPath());
    }

    @Test
    void handleRoleNotFoundException() {
        //given
        String message="Role not found";
        RoleNotFoundException roleNotFoundException=new RoleNotFoundException(message);

        //when
        String path="/test";
        HttpServletRequest request= mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleRoleNotFoundException
                (roleNotFoundException,request);
        //then
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),errorResponse.getStatus());
        Assertions.assertEquals(message,errorResponse.getMessage());
        Assertions.assertEquals(path,errorResponse.getPath());
    }

    @Test
    void handleIncorrectPasswordException() {
        //given
        String message="Incorrect Password";
        IncorrectPasswordException incorrectPasswordException=new IncorrectPasswordException(message);

        //when
        String path="/test";
        HttpServletRequest request= mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleIncorrectPasswordException
                (incorrectPasswordException,request);
        //then
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(),errorResponse.getStatus());
        Assertions.assertEquals(message,errorResponse.getMessage());
        Assertions.assertEquals(path,errorResponse.getPath());
    }

    @Test
    void handleInvalidAuthenticationException() {
        //given
        InvalidAuthenticationException invalidAuthenticationException=new InvalidAuthenticationException();

        //when
        String path="/test";
        HttpServletRequest request= mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<Object> response=globalExceptionHandler.handleInvalidAuthenticationException
                (invalidAuthenticationException,request);
        //then
        ErrorResponse errorResponse=(ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.name(),errorResponse.getError());
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(),errorResponse.getStatus());
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
                (methodArgumentNotValidException,headers, HttpStatus.BAD_REQUEST,servletWebRequest);
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