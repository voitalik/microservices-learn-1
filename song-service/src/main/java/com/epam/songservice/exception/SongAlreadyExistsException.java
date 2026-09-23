package com.epam.songservice.exception;

public class SongAlreadyExistsException extends RuntimeException {
    public SongAlreadyExistsException(Integer id) {
        super("Song metadata with ID=" + id + " already exists");
    }
}
