package com.toyota.errorlistingservice.advice;

import com.toyota.errorlistingservice.exceptions.AttributeNotFoundException;
import com.toyota.errorlistingservice.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AttributeNotFoundException.class)
    public ResponseEntity<Object> handleAttributeNotFoundException(AttributeNotFoundException e)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.BAD_REQUEST,e.getMessage(),getRequestPath());

        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException e)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.UNAUTHORIZED,e.getMessage(),getRequestPath());

        return new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);
    }
    private String getRequestPath() {
        return ((ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes())
                .getRequest().getRequestURI();
    }
}
