package com.toyota.errorlistingservice.exceptions;
/**
 * ImageProcessingException thrown when an IOException occurs
 */
public class ImageProcessingException extends RuntimeException{
    public ImageProcessingException(String message) {
        super(message);
    }
}
