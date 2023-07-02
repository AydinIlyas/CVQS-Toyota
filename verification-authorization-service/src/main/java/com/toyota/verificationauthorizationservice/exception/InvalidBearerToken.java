package com.toyota.verificationauthorizationservice.exception;

public class InvalidBearerToken extends RuntimeException{
    public InvalidBearerToken(String message) {
        super(message);
    }
}
