package com.toyota.verificationauthorizationservice.exception;

public class UsernameTakenException extends RuntimeException{
    public UsernameTakenException(String message) {
        super(message);
    }
}
