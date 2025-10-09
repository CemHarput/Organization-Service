package com.caseStudy.organization_service.controller;


import com.caseStudy.organization_service.dto.*;
import com.caseStudy.organization_service.service.OrganizationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    public ResponseEntity<OrganizationCreateRequestDTO> createUser(@Valid @RequestBody OrganizationCreateRequestDTO request)  {
        OrganizationCreateRequestDTO created = organizationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<OrganizationDTO>> searchOrganizations(
            @ParameterObject OrganizationSearchRequestDTO request,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PageResponse<OrganizationDTO> response =
                organizationService.search(request, pageable);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{registryNumber}")
    public ResponseEntity<OrganizationDTO> updateUser(
            @PathVariable String registryNumber,
            @Valid @RequestBody OrganizationUpdateRequestDTO request
    )  {
        OrganizationDTO updated = organizationService.update(registryNumber, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/registryNumber/{registryNumber}")
    public ResponseEntity<OrganizationDTO> getOrganizationByRegistryNumber(
            @PathVariable String registryNumber
    ) {
        OrganizationDTO updated = organizationService.getByRegistryNumber(registryNumber);
        return ResponseEntity.ok(updated);
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDTO> getOrganizationById(@PathVariable UUID id)  {
        OrganizationDTO user = organizationService.getByID(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{orgId}/members")
    public ResponseEntity<Void> addMember(@PathVariable UUID orgId, @RequestParam UUID userId) {
        organizationService.addMember(orgId, userId);

        return ResponseEntity.noContent().build();
    }


}
