package com.caseStudy.organization_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record OrganizationSearchRequestDTO(
        @Schema(description = "lowercase alphanumeric", example = "boxingclub",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Pattern(regexp = "^[a-z0-9]*$", message = "normalizedName must be lowercase alphanumeric")
        String normalizedName,

        @Schema(example = "2015", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Min(1800) @Max(2100)
        Integer yearFounded,

        @Schema(example = "50", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Min(1)
        Integer companySize

) {
}
