package com.toyota.errorlistingservice.exceptions;

/**
 * DefectNotFoundException thrown when defect not found
 */
public class DefectNotFoundException extends RuntimeException{

    public DefectNotFoundException(String message) {
        super(message);
    }
}
