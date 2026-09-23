package com.epam.resource.service.impl;

import com.epam.resource.client.SongClient;
import com.epam.resource.dto.DeletedResourcesResponse;
import com.epam.resource.dto.ResourceResponse;
import com.epam.resource.dto.SongMetadata;
import com.epam.resource.exception.InvalidResourceException;
import com.epam.resource.service.Mp3MetadataExtractor;
import com.epam.resource.service.OrchestratorService;
import com.epam.resource.service.ResourceIdValidator;
import com.epam.resource.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrchestratorServiceImpl implements OrchestratorService {

    private final Mp3MetadataExtractor metadataExtractor;
    private final ResourceIdValidator idValidator;
    private final ResourceService resourceService;
    private final SongClient songClient;

    @Override
    public ResourceResponse create(String contentType, byte[] data) {
        validateContentType(contentType);
        SongMetadata metadata = metadataExtractor.extract(data);
        ResourceResponse response = resourceService.create(data);
        songClient.create(new SongMetadata(response.id(), metadata.name(), metadata.artist(),
                metadata.album(), metadata.duration(), metadata.year()));

        return response;
    }

    @Override
    public DeletedResourcesResponse deleteAll(String ids) {
        var requestedIds = idValidator.parseCsv(ids);
        DeletedResourcesResponse response = resourceService.deleteAll(requestedIds);
        if (!response.ids().isEmpty()) {
            songClient.deleteAll(ids);
        }

        return response;
    }

    private void validateContentType(String contentType) {
        try {
            if (contentType != null) {
                MediaType type = MediaType.parseMediaType(contentType);
                if ("audio".equalsIgnoreCase(type.getType())
                        && "mpeg".equalsIgnoreCase(type.getSubtype())) {
                    return;
                }
            }
        } catch (InvalidMediaTypeException ignored) {

        }

        throw new InvalidResourceException("Invalid file format: " + contentType
                + ". Only MP3 " + "files are allowed");
    }
}
