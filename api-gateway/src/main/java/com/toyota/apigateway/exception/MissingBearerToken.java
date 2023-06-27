package com.toyota.apigateway.exception;

public class MissingBearerToken extends RuntimeException{
    public MissingBearerToken(String message) {
        super(message);
    }
}
