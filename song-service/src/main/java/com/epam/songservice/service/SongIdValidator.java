package com.epam.songservice.service;

import com.epam.songservice.exception.InvalidSongIdException;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SongIdValidator {
    public Integer parse(String value) {
        if (value == null || !value.matches("[0-9]+")) {
            throw invalidId(value);
        }
        try {
            int id = Integer.parseInt(value);
            if (id <= 0) {
                throw invalidId(value);
            }
            return id;
        } catch (NumberFormatException exception) {
            throw invalidId(value);
        }
    }

    public List<Integer> parseCsv(String value) {
        if (value == null || value.isEmpty()) {
            throw new InvalidSongIdException(
                    "Song IDs must be a non-empty comma-separated list of positive integers");
        }
        if (value.length() > 200) {
            throw new InvalidSongIdException("CSV string length must not exceed 200 characters");
        }
        return Arrays.stream(
                value.split(",", -1))
                .map(this::parse)
                .distinct()
                .toList();
    }

    private InvalidSongIdException invalidId(String value) {
        return new InvalidSongIdException("Invalid song ID: " + value + ". ID must be a positive integer");
    }
}
