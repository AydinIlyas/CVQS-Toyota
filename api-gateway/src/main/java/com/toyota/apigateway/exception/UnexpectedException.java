package com.toyota.apigateway.exception;

public class UnexpectedException extends RuntimeException{
    public UnexpectedException(String message) {
        super(message);
    }
}
