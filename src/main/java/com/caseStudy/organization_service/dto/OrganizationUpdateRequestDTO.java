package com.caseStudy.organization_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;


public record OrganizationUpdateRequestDTO(@Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED,example = "Boxing Club")
                                           @Pattern(regexp = "[A-Za-z0-9 ]*", message = "Organization name must be alphanumeric and spaces")
                                           String organizationName,

                                           @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED,example = "ABC123")
                                           @Pattern(regexp = "[A-Za-z0-9-]*", message = "Registry number must be alphanumeric")
                                           String registryNumber,

                                           @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED,example = "user@example.com")
                                           @Email String contactEmail,

                                           @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED,example = "15")
                                           @Min(value = 1)
                                           Integer companySize,

                                           @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED,example = "2001")
                                           @Min(value = 1800)
                                           @Max(value = 2100)
                                           Integer yearFounded) {
}
