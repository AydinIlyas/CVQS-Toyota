package com.toyota.verificationauthorizationservice.advice;


import com.toyota.verificationauthorizationservice.exception.*;
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
     * Handles UsernameTakenException by returning a ResponseEntity with an appropriate error response.
     * @param ex UsernameTakenException thrown when username is already taken
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler({UsernameTakenException.class})
    public ResponseEntity<Object> handleUsernameTakenException(UsernameTakenException ex, HttpServletRequest request)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.CONFLICT,ex.getMessage());
        errorResponse.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }

    /**
     * Handles UserNotFoundException by returning a ResponseEntity with an appropriate error response.
     * @param ex UserNotFoundException thrown when user not found
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND,ex.getMessage());
        errorResponse.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).header("exception-type","UserNotFound")
                .body(errorResponse);
    }
    /**
     * Handles InvalidBearerToken by returning a ResponseEntity with an appropriate error response.
     * @param ex InvalidBearerToken thrown when bearer token is invalid
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(InvalidBearerToken.class)
    public ResponseEntity<Object> handleInvalidBearerToken(InvalidBearerToken ex, HttpServletRequest request)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND,ex.getMessage());
        errorResponse.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    /**
     * Handles NoRolesException by returning a ResponseEntity with an appropriate error response.
     * @param ex NoRolesException thrown when the user tries to register with no roles
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(NoRolesException.class)
    public ResponseEntity<Object> handleNoRolesException(NoRolesException ex, HttpServletRequest request)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
        errorResponse.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles RoleNotFoundException by returning a ResponseEntity with an appropriate error response.
     * @param ex RoleNotFoundException thrown when no role is found
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException ex, HttpServletRequest request)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND,ex.getMessage());
        errorResponse.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).header("exception-type","RoleNotFound")
                .body(errorResponse);
    }
    /**
     * Handles IncorrectPasswordException by returning a ResponseEntity with an appropriate error response.
     * @param ex IncorrectPasswordException thrown when user tries to update password with wrong password
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<Object> handleIncorrectPasswordException(IncorrectPasswordException ex, HttpServletRequest request)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.UNAUTHORIZED,ex.getMessage());
        errorResponse.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles InvalidAuthenticationException by returning a ResponseEntity with an appropriate error response.
     * @param ex InvalidAuthenticationException thrown when login fails
     * @return ResponseEntity with an ErrorResponse containing details of the error
     */
    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<Object> handleInvalidAuthenticationException(InvalidAuthenticationException ex,
                                                                       HttpServletRequest request)
    {
        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.UNAUTHORIZED,ex.getMessage());
        errorResponse.setPath(request.getRequestURI());
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
