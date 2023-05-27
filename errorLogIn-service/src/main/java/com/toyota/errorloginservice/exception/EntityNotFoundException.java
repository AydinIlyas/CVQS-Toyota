package com.toyota.errorloginservice.exception;

/**
 * Custom EntityNotFoundException
 */
public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String message) {
        super(message);
    }
}
