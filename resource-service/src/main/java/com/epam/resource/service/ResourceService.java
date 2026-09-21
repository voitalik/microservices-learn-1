package com.epam.resource.service;

import com.epam.resource.dto.DeletedResourcesResponse;
import com.epam.resource.dto.ResourceResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ResourceService {

    ResourceResponse create(byte[] data);

    byte[] get(Integer id);

    DeletedResourcesResponse deleteAll(List<Integer> requestedIds);
}
