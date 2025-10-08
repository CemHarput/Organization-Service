package com.caseStudy.organization_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "organization_members",
        uniqueConstraints = @UniqueConstraint(name="uq_org_user", columnNames={"organization_id","user_id"}),
        indexes = {
                @Index(name="idx_org", columnList="organization_id"),
                @Index(name="idx_user", columnList="user_id")
        })
public class OrganizationMember extends AuditBase{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @NotNull
    @Column(name = "organization_id", nullable = false,updatable = false, columnDefinition = "uuid")
    private UUID organizationId;

    @NotNull
    @Column(name = "user_id", nullable = false,updatable = false, columnDefinition = "uuid")
    private UUID userId;


    @Column(name="joined_at", nullable=false, updatable=false)
    private Instant joinedAt = Instant.now();


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }


    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrganizationMember that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getOrganizationId(), that.getOrganizationId()) && Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getJoinedAt(), that.getJoinedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrganizationId(), getUserId(), getJoinedAt());
    }
}
