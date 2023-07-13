package com.toyota.terminalservice.exception;

/**
 * TerminalNotFoundException thrown when terminal not found
 */
public class TerminalNotFoundException extends RuntimeException{
    public TerminalNotFoundException(String message) {
        super(message);
    }
}
