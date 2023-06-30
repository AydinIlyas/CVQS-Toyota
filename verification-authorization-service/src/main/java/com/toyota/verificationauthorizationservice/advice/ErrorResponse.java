package com.toyota.verificationauthorizationservice.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<ValidationError> subErrors;
    public ErrorResponse() {
        this.date =LocalDateTime.now();
    }
    public ErrorResponse(HttpStatus status,String message)
    {
        this();
        this.status=status.value();
        this.error=status.name();
        this.message=message;
        this.path=getRequestPath();
    }
    public ErrorResponse(HttpStatus status,String message,Exception ex)
    {
        this();
        this.status=status.value();
        this.error=status.name();
        this.message=message;
        this.debugMessage=ex.getLocalizedMessage();
        this.path=getRequestPath();
    }
    public void addValidationError(List<FieldError> errors)
    {
        if(subErrors==null)
        {
            subErrors=new ArrayList<>();
        }
        for(FieldError error:errors)
        {
            ValidationError validationError=new ValidationError(error.getObjectName(),error.getDefaultMessage());
            validationError.setField(error.getField());
            validationError.setRejectedValue(error.getRejectedValue());
            subErrors.add(validationError);
        }
    }

    /**
     * @return Request path
     */
    private String getRequestPath() {
        return ((ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes())
                .getRequest().getRequestURI();
    }
}
