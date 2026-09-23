package com.epam.resource.service.impl;

import static java.util.stream.Collectors.toSet;

import com.epam.resource.dto.DeletedResourcesResponse;
import com.epam.resource.dto.ResourceResponse;
import com.epam.resource.entity.Resource;
import com.epam.resource.exception.ResourceNotFoundException;
import com.epam.resource.repository.ResourceRepository;
import com.epam.resource.service.ResourceIdValidator;
import com.epam.resource.service.ResourceService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceIdValidator idValidator;
    private final ResourceRepository repository;

    @Override
    @Transactional
    public ResourceResponse create(byte[] data) {
        Resource resource = repository.save(new Resource(data));

        return new ResourceResponse(resource.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] get(String value) {
        var id = idValidator.parse(value);

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id))
                .getData();
    }

    @Override
    @Transactional
    public DeletedResourcesResponse deleteAll(List<Integer> requestedIds) {
        var resources = repository.findAllById(requestedIds);
        repository.deleteAll(resources);
        var deletedIds = getDeletedIds(requestedIds, resources);

        return new DeletedResourcesResponse(deletedIds);
    }

    private List<Integer> getDeletedIds(List<Integer> requestedIds,
                                        List<Resource> resources) {
        Set<Integer> existingIds = resources.stream()
                .map(Resource::getId)
                .collect(toSet());

        return requestedIds.stream()
                .filter(existingIds::contains)
                .toList();
    }

}
