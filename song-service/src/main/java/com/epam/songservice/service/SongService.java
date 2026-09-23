package com.epam.songservice.service;

import com.epam.songservice.dto.DeletedSongsResponse;
import com.epam.songservice.dto.SongDto;
import com.epam.songservice.dto.SongResponse;

public interface SongService {

    SongResponse create(SongDto song);

    SongDto get(Integer id);

    DeletedSongsResponse deleteAll(String ids);
}
