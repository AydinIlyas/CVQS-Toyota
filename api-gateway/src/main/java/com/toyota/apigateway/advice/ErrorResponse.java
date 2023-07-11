package com.toyota.apigateway.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Class for the error message in json
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime date;
    private int status;
    private String error;
    private String message;
    private String debugMessage;
    private String path;
    public ErrorResponse() {
        this.date =LocalDateTime.now();
    }
    public ErrorResponse(HttpStatus status,String message)
    {
        this();
        this.status=status.value();
        this.error=status.name();
        this.message=message;
    }
    public ErrorResponse(HttpStatus status,String message,Exception ex)
    {
        this();
        this.status=status.value();
        this.error=status.name();
        this.message=message;
        this.debugMessage=ex.getLocalizedMessage();
    }
    public ErrorResponse(HttpStatus status,String message,String path)
    {
        this();
        this.status=status.value();
        this.error=status.name();
        this.message=message;
        this.path=path;
    }


}
