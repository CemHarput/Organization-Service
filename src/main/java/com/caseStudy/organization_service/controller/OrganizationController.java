package com.caseStudy.organization_service.controller;


import com.caseStudy.organization_service.dto.*;
import com.caseStudy.organization_service.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@Tag(name = "organization-controller", description = "Organization CRUD and membership operations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new organization",
            description = "Creates a new organization with a unique registry number."
    )
    public ResponseEntity<OrganizationCreateRequestDTO> createOrganization(@Valid @RequestBody OrganizationCreateRequestDTO request)  {
        OrganizationCreateRequestDTO created = organizationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search organizations",
            description = "Searches organizations by fields such as name and registry number and returns a paged result. Results are sorted by createdAt in descending order by default."
    )
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
    @Operation(
            summary = "Update an organization",
            description = "Updates fields (name, contact email, company size, etc.) of the organization identified by registry number."
    )
    public ResponseEntity<OrganizationDTO> updateOrganization(
            @PathVariable String registryNumber,
            @Valid @RequestBody OrganizationUpdateRequestDTO request
    )  {
        OrganizationDTO updated = organizationService.update(registryNumber, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/registryNumber/{registryNumber}")
    @Operation(
            summary = "Get by registry number",
            description = "Returns a single organization by its registry number."
    )
    public ResponseEntity<OrganizationDTO> getOrganizationByRegistryNumber(
            @PathVariable String registryNumber
    ) {
        OrganizationDTO updated = organizationService.getByRegistryNumber(registryNumber);
        return ResponseEntity.ok(updated);
    }
    @GetMapping("/{id}")
    @Operation(
            summary = "Get by ID",
            description = "Returns organization details by UUID."
    )
    public ResponseEntity<OrganizationDTO> getOrganizationById(@PathVariable UUID id)  {
        OrganizationDTO user = organizationService.getByID(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{orgId}/members")
    @Operation(
            summary = "Add a member to organization",
            description = "Adds the given userId as a member of the specified organization."
    )
    public ResponseEntity<Void> addMember(@PathVariable UUID orgId, @RequestParam UUID userId) {
        organizationService.addMember(orgId, userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/{userId}")
    @Operation(
            summary = "List organizations that a user belongs to",
            description = "Returns a paged list of organizations derived from the memberships of the specified user."
    )
    public ResponseEntity<PageResponse<OrganizationDTO>> listUserOrganizations(
            @PathVariable UUID userId,
            @ParameterObject
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(organizationService.listOrganizationsByUserId(userId, pageable));
    }


}
