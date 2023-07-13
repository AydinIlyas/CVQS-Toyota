package com.toyota.errorloginservice.exception;

/**
 * VehicleAlreadyExistsException thrown when vehicle already exists
 */
public class VehicleAlreadyExistsException extends RuntimeException{
    public VehicleAlreadyExistsException(String message) {
        super(message);
    }
}
