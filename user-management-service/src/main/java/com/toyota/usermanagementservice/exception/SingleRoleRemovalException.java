package com.toyota.usermanagementservice.exception;

/**
 * SingleRoleRemovalException thrown when user tries to remove role but user has only one role
 */
public class SingleRoleRemovalException extends RuntimeException{
    public SingleRoleRemovalException() {
        super("Cannot remove role. User must have at least one role!");
    }
}
