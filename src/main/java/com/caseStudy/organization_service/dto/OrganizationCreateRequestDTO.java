package com.caseStudy.organization_service.dto;

import com.caseStudy.organization_service.model.Organization;

public record OrganizationCreateRequestDTO (String organizationName, String registryNumber, String contactEmail, int companySize, int yearFounded){
    public Organization toEntity() {
        Organization org = new Organization();
        org.setOrganizationName(this.organizationName);
        org.setRegistryNumber(this.registryNumber);
        org.setContactEmail(this.contactEmail);
        org.setCompanySize(this.companySize);
        org.setYearFounded(this.yearFounded);
        return org;
    }
}
