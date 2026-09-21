package com.epam.resource.service;

import com.epam.resource.dto.DeletedResourcesResponse;
import com.epam.resource.dto.ResourceResponse;

public interface ResourceService {

    ResourceResponse create(String contentType, byte[] data);

    byte[] get(Integer id);

    DeletedResourcesResponse delete(String value);
}
