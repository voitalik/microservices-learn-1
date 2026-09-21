package com.epam.resource.service;

import com.epam.resource.dto.DeletedResourcesResponse;
import com.epam.resource.dto.ResourceResponse;

public interface OrchestratorService {

    ResourceResponse create(String contentType, byte[] data);

    DeletedResourcesResponse deleteAll(String ids);

}
