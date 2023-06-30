package com.toyota.errorlistingservice.advice;

import com.toyota.errorlistingservice.exceptions.BearerTokenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles BearerTokenNotFoundException by returning a ResponseEntity with an appropriate error response.
     * @param e BearerTokenNotFoundException thrown when bearer token is missing for some unexpected reason
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(BearerTokenNotFoundException.class)
    public ResponseEntity<Object> handleBearerTokenNotFoundException(BearerTokenNotFoundException e)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.UNAUTHORIZED,e.getMessage());

        return new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);
    }
}
