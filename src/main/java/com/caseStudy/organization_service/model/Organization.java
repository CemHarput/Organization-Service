package com.caseStudy.organization_service.model;


import com.caseStudy.organization_service.util.NameNormalizer;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "organizations",
        indexes = {
                @Index(name = "idx_orgs_normalized_name", columnList = "normalized_name"),
                @Index(name = "idx_orgs_year_founded", columnList = "year_founded"),
                @Index(name = "idx_orgs_company_size", columnList = "company_size"),
                @Index(name = "idx_orgs_contact_email", columnList = "contact_email")
        }
)
public class Organization extends AuditBase{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9 ]+$",
            message = "Organization name must contain only alphanumeric characters and spaces")
    @Column(name = "organization_name", nullable = false, length = 150)
    private String organizationName;

    @NotBlank
    @Column(name = "normalized_name", nullable = false, length = 150)
    private String normalizedName;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "Registry number must be alphanumeric")
    @Column(name = "registry_number", nullable = false, unique = true, length = 100)
    private String registryNumber;

    @Email @NotBlank
    @Column(name = "contact_email", nullable = false, length = 320)
    private String contactEmail;

    @Min(1)
    @Column(name = "company_size")
    private Integer companySize;

    @Min(1800) @Max(2100)
    @Column(name = "year_founded")
    private Integer yearFounded;



    public Organization() {}

    @Override
    protected void beforeSaveOrUpdate() {
        if (organizationName != null) organizationName = organizationName.strip();
        if (normalizedName == null && organizationName != null)
            normalizedName = NameNormalizer.normalize(organizationName);
        if (registryNumber != null) registryNumber = registryNumber.strip();
        if (contactEmail != null) contactEmail = contactEmail.strip().toLowerCase(Locale.ROOT);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getRegistryNumber() {
        return registryNumber;
    }

    public void setRegistryNumber(String registryNumber) {
        this.registryNumber = registryNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Integer getCompanySize() {
        return companySize;
    }

    public void setCompanySize(Integer companySize) {
        this.companySize = companySize;
    }

    public Integer getYearFounded() {
        return yearFounded;
    }

    public void setYearFounded(Integer yearFounded) {
        this.yearFounded = yearFounded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getOrganizationName(), that.getOrganizationName()) && Objects.equals(getNormalizedName(), that.getNormalizedName()) && Objects.equals(getRegistryNumber(), that.getRegistryNumber()) && Objects.equals(getContactEmail(), that.getContactEmail()) && Objects.equals(getCompanySize(), that.getCompanySize()) && Objects.equals(getYearFounded(), that.getYearFounded());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrganizationName(), getNormalizedName(), getRegistryNumber(), getContactEmail(), getCompanySize(), getYearFounded());
    }
}
