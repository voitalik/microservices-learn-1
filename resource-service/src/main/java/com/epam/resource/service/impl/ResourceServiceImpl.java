package com.epam.resource.service.impl;

import com.epam.resource.client.SongClient;
import com.epam.resource.dto.DeletedResourcesResponse;
import com.epam.resource.dto.ResourceResponse;
import com.epam.resource.entity.Resource;
import com.epam.resource.exception.ResourceNotFoundException;
import com.epam.resource.repository.ResourceRepository;
import com.epam.resource.service.Mp3MetadataExtractor;
import com.epam.resource.service.ResourceIdValidator;
import com.epam.resource.service.ResourceService;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository repository;
    private final Mp3MetadataExtractor metadataExtractor;
    private final ResourceIdValidator idValidator;
    private final SongClient songClient;

    @Override
    @Transactional
    public ResourceResponse create(byte[] data) {
        Resource resource = repository.save(new Resource(data));
        return new ResourceResponse(resource.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] get(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id))
                .getData();
    }

    @Transactional
    @Override
    public DeletedResourcesResponse deleteAll(List<Integer> requestedIds) {
        var resources = repository.findAllById(requestedIds);
        var deletedIds = getDeletedIds(requestedIds, resources);
        repository.deleteAll(resources);
        return new DeletedResourcesResponse(deletedIds);
    }

    private List<Integer> getDeletedIds(List<Integer> requestedIds,
                                        List<Resource> resources) {
        var existingIds = new HashSet<Integer>();
        resources.forEach(resource -> existingIds.add(resource.getId()));
        return requestedIds.stream()
                .filter(existingIds::contains)
                .toList();
    }

}
