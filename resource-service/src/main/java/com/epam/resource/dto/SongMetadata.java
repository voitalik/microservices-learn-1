package com.epam.resource.dto;

public record SongMetadata(Integer id, String name,
                           String artist, String album,
                           String duration, String year) {
}
