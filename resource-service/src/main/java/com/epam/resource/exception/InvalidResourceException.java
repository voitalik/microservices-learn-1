package com.epam.resource.exception;

public class InvalidResourceException extends RuntimeException {
    public InvalidResourceException(String message) {
        super(message);
    }
}
