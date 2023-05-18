package com.toyota.errorloginservice.advice;

import com.toyota.errorloginservice.exception.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(HttpServletRequest request, EntityNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), getRequestPath(request));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleInvalidArgument(HttpServletRequest request, MethodArgumentNotValidException ex) {
//        Map<String, String> errorMap = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error -> {
//            errorMap.put(error.getField(), error.getDefaultMessage());
//        });
//        StringBuilder builder=new StringBuilder();
//        for(Map.Entry<String,String> set :errorMap.entrySet())
//        {
//            builder.append(set.getKey()+": "+set.getValue()+" ");
//        }
//        ErrorResponse errorResponse=new ErrorResponse(HttpStatus.BAD_REQUEST,builder.toString(),getRequestPath(request));
//        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
//    }
@Override
protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("error", "Invalid JSON input");
    responseBody.put("details", ex.getBindingResult().getAllErrors());

    return ResponseEntity.badRequest().body(responseBody);
}
    private String getRequestPath(HttpServletRequest request) {
        return ((ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes())
                .getRequest().getRequestURI();
    }
}
