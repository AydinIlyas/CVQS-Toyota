package com.toyota.verificationauthorizationservice.exception;

public class InvalidAuthenticationException extends RuntimeException{
    public InvalidAuthenticationException() {
        super("Authentication failed! The provided username or password is incorrect.");
    }
}
