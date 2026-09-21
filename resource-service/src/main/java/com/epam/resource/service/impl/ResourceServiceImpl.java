package com.epam.resource.service.impl;

import com.epam.resource.client.SongClient;
import com.epam.resource.dto.DeletedResourcesResponse;
import com.epam.resource.dto.ResourceResponse;
import com.epam.resource.dto.SongMetadata;
import com.epam.resource.entity.Resource;
import com.epam.resource.exception.InvalidResourceException;
import com.epam.resource.exception.ResourceNotFoundException;
import com.epam.resource.repository.ResourceRepository;
import com.epam.resource.service.Mp3MetadataExtractor;
import com.epam.resource.service.ResourceIdValidator;
import com.epam.resource.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository repository;
    private final Mp3MetadataExtractor metadataExtractor;
    private final ResourceIdValidator idValidator;
    private final SongClient songClient;

    @Override
    @Transactional
    public ResourceResponse create(String contentType, byte[] data) {
        validateContentType(contentType);
        SongMetadata metadata = metadataExtractor.extract(data);
        Resource resource = repository.save(new Resource(data));
        songClient.create(new SongMetadata(resource.getId(), metadata.name(), metadata.artist(),
                metadata.album(), metadata.duration(), metadata.year()));
        return new ResourceResponse(resource.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] get(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id))
                .getData();
    }

    @Override
    @Transactional
    public DeletedResourcesResponse delete(String value) {
        var requestedIds = idValidator.parseCsv(value);
        var resources = repository.findAllById(requestedIds);
        var existingIds = new HashSet<Integer>();
        resources.forEach(resource -> existingIds.add(resource.getId()));
        var deletedIds = requestedIds.stream()
                .filter(existingIds::contains)
                .toList();
        if (!deletedIds.isEmpty()) {
            repository.deleteAll(resources);
            String deleteIdsString = deletedIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            songClient.delete(deleteIdsString);
        }
        return new DeletedResourcesResponse(deletedIds);
    }

    private void validateContentType(String contentType) {
        try {
            if (contentType != null) {
                MediaType type = MediaType.parseMediaType(contentType);
                if ("audio".equalsIgnoreCase(type.getType()) && "mpeg".equalsIgnoreCase(type.getSubtype())) {
                    return;
                }
            }
        } catch (InvalidMediaTypeException ignored) {

        }
        throw new InvalidResourceException("Invalid file format: " + contentType + ". Only MP3 files are allowed");
    }
}
