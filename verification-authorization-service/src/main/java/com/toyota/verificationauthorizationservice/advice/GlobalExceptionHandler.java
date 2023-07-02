package com.toyota.verificationauthorizationservice.advice;


import com.toyota.verificationauthorizationservice.exception.*;
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

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    Logger logger= LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles UsernameTakenException by returning a ResponseEntity with an appropriate error response.
     * @param e UsernameTakenException thrown when username is already taken
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(UsernameTakenException.class)
    public ResponseEntity<Object> handleUsernameTakenException(UsernameTakenException e)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.CONFLICT,e.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }

    /**
     * Handles UserNotFoundException by returning a ResponseEntity with an appropriate error response.
     * @param e UserNotFoundException thrown when user not found
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND,e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).header("exception-type","UserNotFound")
                .body(errorResponse);
    }
    /**
     * Handles InvalidBearerToken by returning a ResponseEntity with an appropriate error response.
     * @param e InvalidBearerToken thrown when bearer token is invalid
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(InvalidBearerToken.class)
    public ResponseEntity<Object> handleBearerTokenNotFound(InvalidBearerToken e)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND,e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    /**
     * Handles NoRolesException by returning a ResponseEntity with an appropriate error response.
     * @param e NoRolesException thrown when the user tries to register with no roles
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(NoRolesException.class)
    public ResponseEntity<Object> handleNoRolesException(NoRolesException e)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.BAD_REQUEST,e.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles RoleNotFoundException by returning a ResponseEntity with an appropriate error response.
     * @param e RoleNotFoundException thrown when no role is found
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException e)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND,e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).header("exception-type","RoleNotFound")
                .body(errorResponse);
    }
    /**
     * Handles IncorrectPasswordException by returning a ResponseEntity with an appropriate error response.
     * @param e IncorrectPasswordException thrown when user tries to update password with wrong password
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<Object> handleIncorrectPasswordException(IncorrectPasswordException e)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.UNAUTHORIZED,e.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles InvalidAuthenticationException by returning a ResponseEntity with an appropriate error response.
     * @param e InvalidAuthenticationException thrown when login fails
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<Object> handleInvalidAuthenticationException(InvalidAuthenticationException e)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.UNAUTHORIZED,e.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);
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
