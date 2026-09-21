package com.epam.resource.controller;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;

import com.epam.resource.dto.DeletedResourcesResponse;
import com.epam.resource.dto.ResourceResponse;
import com.epam.resource.service.OrchestratorService;
import com.epam.resource.service.ResourceService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resources")
@Validated
@RequiredArgsConstructor
public class ResourceController {
    private final OrchestratorService orchestratorService;
    private final ResourceService resourceService;

    @PostMapping
    @ResponseStatus(OK)
    public ResourceResponse upload(
            @RequestHeader(value = CONTENT_TYPE, required = false) String contentType,
            @RequestBody(required = false) byte[] data) {
        return orchestratorService.create(contentType, data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> get(@PathVariable @Positive Integer id) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(resourceService.get(id));
    }

    @DeleteMapping
    @ResponseStatus(OK)
    public DeletedResourcesResponse delete(@RequestParam String id) {
        return orchestratorService.deleteAll(id);
    }
}
