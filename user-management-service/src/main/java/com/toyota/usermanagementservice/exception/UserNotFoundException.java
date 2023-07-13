package com.toyota.usermanagementservice.exception;

/**
 * UserNotFoundException thrown when User not found
 */
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
