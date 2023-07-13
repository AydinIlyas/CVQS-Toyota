package com.toyota.usermanagementservice.exception;

/**
 * RoleAlreadyExistsException thrown when User already has this role
 */
public class RoleAlreadyExistsException extends RuntimeException{
    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}
