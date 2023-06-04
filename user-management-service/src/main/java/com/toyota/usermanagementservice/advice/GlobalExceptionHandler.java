package com.toyota.usermanagementservice.advice;

import com.toyota.usermanagementservice.exception.UnexpectedException;
import com.toyota.usermanagementservice.exception.UserAlreadyExistsException;
import com.toyota.usermanagementservice.exception.UserNotFoundException;
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

/**
 * Class for centralizing exceptions and handle them.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger= LogManager.getLogger(GlobalExceptionHandler.class);
    /**
     * Handles User Not found exception
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex, getRequestPath());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    /**
     * Handles Unexpected exception
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(UnexpectedException.class)
    public ResponseEntity<Object> handleUnexpectedException(UnexpectedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, getRequestPath());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Handles Unexpected exception
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex, getRequestPath());
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

    /**
     * @return Request path
     */
    private String getRequestPath() {
        return ((ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes())
                .getRequest().getRequestURI();
    }
}


