package com.caseStudy.organization_service.dto;

import com.caseStudy.organization_service.model.Organization;

public record OrganizationDTO(String organizationName, String registryNumber, String contactEmail, int companySize, int yearFounded) {

    public static OrganizationDTO convertFromOrganization(Organization organization){
        return new OrganizationDTO(
                organization.getOrganizationName(),
                organization.getRegistryNumber(),
                organization.getContactEmail(),
                organization.getCompanySize(),
                organization.getYearFounded()
        );
    }
}
