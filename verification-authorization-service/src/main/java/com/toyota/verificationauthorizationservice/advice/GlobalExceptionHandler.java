package com.toyota.verificationauthorizationservice.advice;


import com.toyota.verificationauthorizationservice.exception.InvalidAuthenticationException;
import com.toyota.verificationauthorizationservice.exception.NoRolesException;
import com.toyota.verificationauthorizationservice.exception.IncorrectPasswordException;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    Logger logger= LogManager.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(NoRolesException.class)
    public ResponseEntity<Object> handleNoRolesException(NoRolesException e)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND,e.getMessage(),getRequestPath());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<Object> handleIncorrectPasswordException(IncorrectPasswordException e)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.UNAUTHORIZED,e,getRequestPath());
        return new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<Object> handleIncorrectPasswordException(InvalidAuthenticationException e)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.UNAUTHORIZED,e,getRequestPath());
        return new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);
    }
    /**
     * Handles if an argument is invalid.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", "Invalid JSON input");
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        errorResponse.addValidationError(fieldErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
                                                               @NonNull HttpHeaders headers,
                                                               @NonNull HttpStatusCode status,
                                                               @NonNull WebRequest request) {
        ServletWebRequest servletWebRequest=(ServletWebRequest) request;
        logger.info("{} to {}",servletWebRequest.getHttpMethod(),servletWebRequest.getRequest().getServletPath());

        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.BAD_REQUEST,"Malformed Json Request",ex);
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
    private String getRequestPath() {
        return ((ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes())
                .getRequest().getRequestURI();
    }
}
