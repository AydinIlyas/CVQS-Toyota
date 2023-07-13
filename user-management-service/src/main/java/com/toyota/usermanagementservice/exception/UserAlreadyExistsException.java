package com.toyota.usermanagementservice.exception;

/**
 * UserAlreadyExistsException thrown when User already exists
 */
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
