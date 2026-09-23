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
            throw new InvalidSongIdException("CSV string of positive integers is expected");
        }
        if (value.length() > 200) {
            throw new InvalidSongIdException("CSV string is too long: received " + value.length()
                                                     + " characters, maximum allowed is 200");
        }
        return Arrays.stream(value.split(",", -1))
                .map(this::parseCsvItem)
                .distinct()
                .toList();
    }

    private Integer parseCsvItem(String value) {
        try {
            int id = Integer.parseInt(value);
            if (id <= 0) {
                throw invalidId(value);
            }
            return id;
        } catch (NumberFormatException exception) {
            throw new InvalidSongIdException("Invalid ID format: '" + value + "'."
                                                     + " Only positive integers are allowed");
        }
    }

    private InvalidSongIdException invalidId(String value) {
        return new InvalidSongIdException("Invalid value '" + value + "' for ID."
                                                  + " Must be a positive integer");
    }
}
