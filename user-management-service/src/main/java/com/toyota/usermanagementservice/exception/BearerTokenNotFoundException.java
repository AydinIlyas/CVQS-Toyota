package com.toyota.usermanagementservice.exception;

public class BearerTokenNotFoundException extends RuntimeException{
    public BearerTokenNotFoundException(String message) {
        super(message);
    }
}
