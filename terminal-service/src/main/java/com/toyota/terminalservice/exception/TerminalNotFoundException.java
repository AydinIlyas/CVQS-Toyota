package com.toyota.terminalservice.exception;

/**
 * Custom exception for terminal not found
 */
public class TerminalNotFoundException extends RuntimeException{
    public TerminalNotFoundException(String message) {
        super(message);
    }
}
