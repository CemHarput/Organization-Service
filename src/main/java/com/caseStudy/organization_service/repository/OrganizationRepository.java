package com.caseStudy.organization_service.repository;

import com.caseStudy.organization_service.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID>, JpaSpecificationExecutor<Organization> {


    @Query("""
              select o from Organization o
              where lower(o.registryNumber) = lower(:registryNumber)
            """)
    Optional<Organization> findByRegistryNumberIgnoreCase(@Param("registryNumber") String registryNumber);

    boolean existsByRegistryNumber(String registryNumber);
}
