package com.toyota.usermanagementservice.exception;

public class SingleRoleRemovalException extends RuntimeException{
    public SingleRoleRemovalException() {
        super("Cannot remove role. User must have at least one role!");
    }
}
