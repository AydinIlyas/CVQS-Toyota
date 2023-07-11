package com.toyota.terminalservice.advice;

import com.toyota.terminalservice.exception.TerminalAlreadyExistsException;
import com.toyota.terminalservice.exception.TerminalNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles TerminalNotFoundException by returning a ResponseEntity with an appropriate error response.
     * @param ex TerminalNotFoundException thrown when terminal not found
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(TerminalNotFoundException.class)
    public ResponseEntity<Object> handleTerminalNotFoundException(TerminalNotFoundException ex,
                                                                  HttpServletRequest request)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND,ex.getMessage());
        errorResponse.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }
    /**
     * Handles TerminalAlreadyExistsException by returning a ResponseEntity with an appropriate error response.
     * @param ex TerminalAlreadyExistsException thrown when terminal already exists
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(TerminalAlreadyExistsException.class)
    public ResponseEntity<Object> handleTerminalAlreadyExistsException(TerminalAlreadyExistsException ex,
                                                                       HttpServletRequest request)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.CONFLICT,ex.getMessage());
        errorResponse.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }
    /**
     * Handles MethodArgumentNotValidException by returning a ResponseEntity with an appropriate error response.
     *
     * @param ex MethodArgumentNotValidException thrown when there are validation errors for method arguments
     * @param headers HttpHeaders
     * @param status HttpStatusCode
     * @param request WebRequest
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        ServletWebRequest servletWebRequest=(ServletWebRequest) request;
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        errorResponse.addValidationError(fieldErrors);
        errorResponse.setPath(servletWebRequest.getRequest().getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles HttpMessageNotReadableException by returning a ResponseEntity with an appropriate error response.
     * @param ex HttpMessageNotReadableException thrown when input is malformed
     * @param headers HttpHeaders
     * @param status HttpStatusCode
     * @param request WebRequest
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
                                                               @NonNull HttpHeaders headers,
                                                               @NonNull HttpStatusCode status,
                                                               @NonNull WebRequest request) {
        ServletWebRequest servletWebRequest=(ServletWebRequest) request;
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.BAD_REQUEST,"Malformed Json Request",ex);
        errorResponse.setPath(servletWebRequest.getRequest().getRequestURI());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

}
