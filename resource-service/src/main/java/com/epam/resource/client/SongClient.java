package com.epam.resource.client;

import com.epam.resource.dto.SongMetadata;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "song-service", url = "${song-service.url}")
public interface SongClient {

    @PostMapping(value = "/songs")
    void create(@RequestBody SongMetadata metadata);

    @DeleteMapping("/songs")
    void deleteAll(@RequestParam("id") String ids);
}
