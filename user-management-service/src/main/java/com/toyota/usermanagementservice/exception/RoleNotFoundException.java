package com.toyota.usermanagementservice.exception;

/**
 * RoleNotFoundException thrown when Role not found
 */
public class RoleNotFoundException extends RuntimeException{

    public RoleNotFoundException(String message) {
        super(message);
    }
}
