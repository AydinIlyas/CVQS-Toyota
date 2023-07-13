package com.toyota.errorloginservice.exception;

/**
 * EntityNotFoundException thrown when entity not found
 */
public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String message) {
        super(message);
    }
}
