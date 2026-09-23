package com.epam.songservice.controller;

import static org.springframework.http.HttpStatus.OK;

import com.epam.songservice.dto.DeletedSongsResponse;
import com.epam.songservice.dto.SongDto;
import com.epam.songservice.dto.SongResponse;
import com.epam.songservice.service.SongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/songs")
@Validated
@RequiredArgsConstructor
public class SongController {
    private final SongService songService;

    @PostMapping
    @ResponseStatus(OK)
    public SongResponse create(@Valid @RequestBody SongDto song) {
        return songService.create(song);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public SongDto get(@PathVariable String id) {
        return songService.get(id);
    }

    @DeleteMapping
    @ResponseStatus(OK)
    public DeletedSongsResponse delete(@RequestParam String id) {
        return songService.deleteAll(id);
    }
}
