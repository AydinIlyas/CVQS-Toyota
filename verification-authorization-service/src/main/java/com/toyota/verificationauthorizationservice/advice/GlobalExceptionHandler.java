package com.toyota.verificationauthorizationservice.advice;


import com.toyota.verificationauthorizationservice.exception.NoRolesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoRolesException.class)
    public ResponseEntity<Object> handleAttributeNotFoundException(NoRolesException e)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND,e.getMessage(),getRequestPath());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
    private String getRequestPath() {
        return ((ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes())
                .getRequest().getRequestURI();
    }
}
