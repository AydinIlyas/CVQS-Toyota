package com.toyota.errorloginservice.advice;

import com.toyota.errorloginservice.exception.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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

/**
 * Class for centralizing exceptions and handle them.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger logger= LogManager.getLogger(GlobalExceptionHandler.class);
    /**
     * Handles Entity Not found custom exception
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(HttpServletRequest request, EntityNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), getRequestPath(request));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", "Invalid JSON input");
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        errorResponse.addValidationError(fieldErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ServletWebRequest servletWebRequest=(ServletWebRequest) request;
        logger.info("{} to {}",servletWebRequest.getHttpMethod(),servletWebRequest.getRequest().getServletPath());

        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.BAD_REQUEST,"Malformed Json Request",ex);
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    private String getRequestPath(HttpServletRequest request) {
        return ((ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes())
                .getRequest().getRequestURI();
    }
}


