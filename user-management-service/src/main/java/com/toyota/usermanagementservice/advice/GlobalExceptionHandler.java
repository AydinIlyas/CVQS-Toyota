package com.toyota.usermanagementservice.advice;

import com.toyota.usermanagementservice.exception.*;
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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

/**
 * Class for centralizing exceptions and handle them.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger= LogManager.getLogger(GlobalExceptionHandler.class);
    /**
     * Handles UserNotFoundException by returning a ResponseEntity with an appropriate error response.
     *
     * @param ex UserNotFoundException thrown when a user is not found.
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    /**
     * Handles Unexpected exception by returning a ResponseEntity with an appropriate error response.
     *
     * @param ex UnexpectedException thrown when an unexpected error occurs.
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(UnexpectedException.class)
    public ResponseEntity<Object> handleUnexpectedException(UnexpectedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(),ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Handles Unexpected exception by returning a ResponseEntity with an appropriate error response.
     *
     * @param ex UserAlreadyExistsException thrown when username or email already exists.
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles SingleRoleRemovalException by returning a ResponseEntity with an appropriate error response.
     * @param ex SingleRoleRemovalException thrown when you try to remove role and user has only one role
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(SingleRoleRemovalException.class)
    public ResponseEntity<Object> handleSingleRoleRemovalException(SingleRoleRemovalException ex)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles RoleAlreadyExistsException by returning a ResponseEntity with an appropriate error response.
     * @param ex RoleAlreadyExistsException thrown when you try to add a role which the user already has.
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<Object> handleRoleAlreadyExistsException(RoleAlreadyExistsException ex)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles RoleNotFoundException by returning a ResponseEntity with an appropriate error response.
     * @param ex RoleNotFoundException thrown when no role is found
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException ex)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BearerTokenNotFoundException.class)
    public ResponseEntity<Object> handleBearerTokenNotFoundException(BearerTokenNotFoundException ex)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
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
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        errorResponse.addValidationError(fieldErrors);

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
        logger.info("{} to {}",servletWebRequest.getHttpMethod(),servletWebRequest.getRequest().getServletPath());

        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.BAD_REQUEST,"Malformed Json Request",ex);
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
}


