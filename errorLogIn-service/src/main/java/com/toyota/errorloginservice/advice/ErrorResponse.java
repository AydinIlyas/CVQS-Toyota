package com.toyota.errorloginservice.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Getter
@Setter
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime date;
    private int status;
    private String error;
    private String message;
    private String path;

    public ErrorResponse() {
        this.date =LocalDateTime.now();
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
