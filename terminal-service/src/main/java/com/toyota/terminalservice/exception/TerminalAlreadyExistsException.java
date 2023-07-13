package com.toyota.terminalservice.exception;

/**
 * TerminalAlreadyExistsException thrown when terminal already exists
 */
public class TerminalAlreadyExistsException extends RuntimeException{
    public TerminalAlreadyExistsException(String message) {
        super(message);
    }
}
