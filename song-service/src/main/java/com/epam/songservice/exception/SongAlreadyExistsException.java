package com.epam.songservice.exception;

public class SongAlreadyExistsException extends RuntimeException {
    public SongAlreadyExistsException(Integer id) {
        super("Metadata for resource ID=" + id + " already exists");
    }
}
