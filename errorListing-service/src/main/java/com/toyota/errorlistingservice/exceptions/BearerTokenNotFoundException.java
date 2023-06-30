package com.toyota.errorlistingservice.exceptions;

public class BearerTokenNotFoundException extends RuntimeException{
    public BearerTokenNotFoundException(String message) {
        super(message);
    }
}
