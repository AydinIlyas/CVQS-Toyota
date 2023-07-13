package com.toyota.errorloginservice.exception;

/**
 * InvalidLocationException thrown when Locations are not in the image
 */
public class InvalidLocationException extends RuntimeException{
    public InvalidLocationException(String message) {
        super(message);
    }
}
