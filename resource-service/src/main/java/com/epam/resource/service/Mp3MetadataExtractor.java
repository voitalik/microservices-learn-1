package com.epam.resource.service;

import com.epam.resource.dto.SongMetadata;

public interface Mp3MetadataExtractor {

    SongMetadata extract(byte[] data);

}
