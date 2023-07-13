package com.toyota.errorloginservice.exception;

/**
 * ImageNotFoundException thrown when image not found
 */
public class ImageNotFoundException extends RuntimeException{
    public ImageNotFoundException(String message) {
        super(message);
    }
}
