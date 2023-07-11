package com.toyota.errorlistingservice.advice;

import com.toyota.errorlistingservice.exceptions.BearerTokenNotFoundException;
import com.toyota.errorlistingservice.exceptions.DefectNotFoundException;
import com.toyota.errorlistingservice.exceptions.ImageProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles BearerTokenNotFoundException by returning a ResponseEntity with an appropriate error response.
     * @param e BearerTokenNotFoundException thrown when bearer token is missing for some unexpected reason
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(BearerTokenNotFoundException.class)
    public ResponseEntity<Object> handleBearerTokenNotFoundException(BearerTokenNotFoundException e,
                                                                     HttpServletRequest request)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.UNAUTHORIZED,e.getMessage());
        errorResponse.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles DefectNotFoundException by returning a ResponseEntity with an appropriate error response.
     * @param e DefectNotFoundException thrown when defect does not exist.
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(DefectNotFoundException.class)
    public ResponseEntity<Object> handleDefectNotFoundException(DefectNotFoundException e,HttpServletRequest request)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND,e.getMessage());
        errorResponse.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    /**
     * Handles ImageProcessingException by returning a ResponseEntity with an appropriate error response.
     * @param e ImageProcessingException thrown when defect does not exist.
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(ImageProcessingException.class)
    public ResponseEntity<Object> handleImageProcessingException(ImageProcessingException e,HttpServletRequest request)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        errorResponse.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
