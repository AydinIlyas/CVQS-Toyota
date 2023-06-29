package com.toyota.errorloginservice.exception;

public class InvalidLocationException extends RuntimeException{
    public InvalidLocationException(String message) {
        super(message);
    }
}
