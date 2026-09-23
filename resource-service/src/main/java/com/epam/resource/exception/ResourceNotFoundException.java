package com.epam.resource.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Integer id) {
        super("Resource with ID=" + id + " not found");
    }
}
