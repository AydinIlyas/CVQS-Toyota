package com.toyota.errorloginservice.exception;

public class VehicleAlreadyExistsException extends RuntimeException{
    public VehicleAlreadyExistsException(String message) {
        super(message);
    }
}
