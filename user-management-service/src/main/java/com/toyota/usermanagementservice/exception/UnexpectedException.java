package com.toyota.usermanagementservice.exception;

/**
 * Thrown when something unexpected happens
 */
public class UnexpectedException extends RuntimeException{
    public UnexpectedException(String message) {
        super(message);
    }
}
