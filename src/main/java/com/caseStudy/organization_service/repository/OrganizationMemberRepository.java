package com.caseStudy.organization_service.repository;

import com.caseStudy.organization_service.model.OrganizationMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, UUID> {

    boolean existsByOrganizationIdAndUserId(UUID organizationId, UUID userId);
}
