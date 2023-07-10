package com.toyota.terminalservice.exception;

public class TerminalAlreadyExistsException extends RuntimeException{
    public TerminalAlreadyExistsException(String message) {
        super(message);
    }
}
