package com.caseStudy.organization_service.repository;

import com.caseStudy.organization_service.model.Organization;
import com.caseStudy.organization_service.model.OrganizationMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, UUID> {


    @Query("""
    select o
    from Organization o
    where o.id in (
      select m.organizationId
      from OrganizationMember m
      where m.userId = :userId
    )
  """)
    Page<Organization> findAllByUserId(@Param("userId") UUID userId, Pageable pageable);
    boolean existsByOrganizationIdAndUserId(UUID organizationId, UUID userId);
}
