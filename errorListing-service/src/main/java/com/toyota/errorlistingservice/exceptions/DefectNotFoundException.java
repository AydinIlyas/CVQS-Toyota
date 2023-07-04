package com.toyota.errorlistingservice.exceptions;

public class DefectNotFoundException extends RuntimeException{

    public DefectNotFoundException(String message) {
        super(message);
    }
}
