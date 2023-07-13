package com.toyota.errorloginservice.exception;

/**
 * ImageProcessingException thrown when any exception occurs while processing image.
 */
public class ImageProcessingException extends RuntimeException{
    public ImageProcessingException(String message) {
        super(message);
    }
}
