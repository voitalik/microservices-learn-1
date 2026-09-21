package com.epam.resource.client;

import com.epam.resource.dto.SongMetadata;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "song-service", url = "${song-service.url}")
public interface SongClient {

    @PostMapping(value = "/songs")
    void create(@RequestBody SongMetadata metadata);

    @DeleteMapping("/songs")
    void delete(@RequestParam("id") String ids);
}
