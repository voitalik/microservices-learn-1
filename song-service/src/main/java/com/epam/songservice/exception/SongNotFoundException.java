package com.epam.songservice.exception;

public class SongNotFoundException extends RuntimeException {
    public SongNotFoundException(Integer id) {
        super("Song metadata with ID=" + id + " not found");
    }
}
