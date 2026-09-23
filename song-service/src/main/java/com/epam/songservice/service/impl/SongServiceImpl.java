package com.epam.songservice.service.impl;

import static java.util.stream.Collectors.toSet;

import com.epam.songservice.dto.DeletedSongsResponse;
import com.epam.songservice.dto.SongDto;
import com.epam.songservice.dto.SongResponse;
import com.epam.songservice.entity.Song;
import com.epam.songservice.exception.SongAlreadyExistsException;
import com.epam.songservice.exception.SongNotFoundException;
import com.epam.songservice.repository.SongRepository;
import com.epam.songservice.service.SongIdValidator;
import com.epam.songservice.service.SongService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final SongRepository repository;
    private final SongIdValidator idValidator;

    @Override
    @Transactional
    public SongResponse create(SongDto song) {
        int inserted = repository.insert(song.id(), song.name(), song.artist(),
                                         song.album(), song.duration(), song.year());
        if (inserted == 0) {
            throw new SongAlreadyExistsException(song.id());
        }

        return new SongResponse(song.id());
    }

    @Override
    @Transactional(readOnly = true)
    public SongDto get(Integer id) {
        var song = repository.findById(id)
                .orElseThrow(() -> new SongNotFoundException(id));

        return new SongDto(song.getId(), song.getName(), song.getArtist(),
                           song.getAlbum(), song.getDuration(), song.getYear());
    }

    @Override
    @Transactional(readOnly = true)
    public DeletedSongsResponse deleteAll(String ids) {
        var requestedIds = idValidator.parseCsv(ids);
        var songs = repository.findAllById(requestedIds);
        repository.deleteAll(songs);
        var deletedIds = getDeletedIds(requestedIds, songs);

        return new DeletedSongsResponse(deletedIds);
    }

    private List<Integer> getDeletedIds(List<Integer> requestedIds,
                                        List<Song> songs) {
        Set<Integer> existingIds = songs.stream()
                .map(Song::getId)
                .collect(toSet());

        return requestedIds.stream()
                .filter(existingIds::contains)
                .toList();
    }
}
