package com.epam.resource.service;

import com.epam.resource.dto.DeletedResourcesResponse;
import com.epam.resource.dto.ResourceResponse;
import java.util.List;

public interface ResourceService {

    ResourceResponse create(byte[] data);

    byte[] get(String value);

    DeletedResourcesResponse deleteAll(List<Integer> requestedIds);
}
