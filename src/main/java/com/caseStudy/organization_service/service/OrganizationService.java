package com.caseStudy.organization_service.service;


import com.caseStudy.organization_service.dto.*;
import com.caseStudy.organization_service.exception.OrgAlreadyExistsException;
import com.caseStudy.organization_service.exception.OrgNotFoundException;
import com.caseStudy.organization_service.model.Organization;
import com.caseStudy.organization_service.repository.OrganizationMemberRepository;
import com.caseStudy.organization_service.repository.OrganizationRepository;
import com.caseStudy.organization_service.util.NameNormalizer;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class OrganizationService {

    private static final Logger log = LoggerFactory.getLogger(OrganizationService.class);

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }


    public OrganizationCreateRequestDTO create(OrganizationCreateRequestDTO organizationCreateRequestDTO) {
        log.debug("Creating organization with email: {}", organizationCreateRequestDTO.registryNumber());
        if (organizationRepository.existsByRegistryNumber(organizationCreateRequestDTO.registryNumber()))
            throw new OrgAlreadyExistsException(organizationCreateRequestDTO.contactEmail());


        Organization org = new Organization();

        org.setOrganizationName(organizationCreateRequestDTO.organizationName());
        org.setRegistryNumber(organizationCreateRequestDTO.registryNumber());
        org.setContactEmail(organizationCreateRequestDTO.contactEmail());
        org.setCompanySize(organizationCreateRequestDTO.companySize());
        org.setYearFounded(organizationCreateRequestDTO.yearFounded());

        organizationRepository.save(org);
        log.info("Organization created successfully with id: {}", org.getId());

        return organizationCreateRequestDTO;


    }
    @Transactional(readOnly = true)
    public OrganizationDTO getByRegistryNumber(String registryNumber) {
        log.debug("Fetching org by registryNumber: {}", registryNumber);
        Organization u = organizationRepository.findByRegistryNumberIgnoreCase(registryNumber)
                .orElseThrow(() -> new OrgNotFoundException("Organization could not found"));
        return OrganizationDTO.convertFromOrganization(u);
    }
    public OrganizationDTO update(String registryNumber, OrganizationUpdateRequestDTO req) {
        log.debug("Organization update started with {}:",registryNumber);
        var org = organizationRepository
                .findByRegistryNumberIgnoreCase(registryNumber)
                .orElseThrow(() -> new OrgNotFoundException("Organization could not found"));

        if (Objects.nonNull(req.organizationName()) && !req.organizationName().isBlank()) {
            org.setOrganizationName(req.organizationName());
            org.setNormalizedName(NameNormalizer.normalize(req.organizationName()));
        }

        if (Objects.nonNull(req.registryNumber()) && !req.registryNumber().isBlank()) {

            organizationRepository.findByRegistryNumberIgnoreCase(req.registryNumber())
                    .filter(o -> !o.getId().equals(org.getId()))
                    .ifPresent(o -> {
                        throw new OrgAlreadyExistsException("Registry number in use");
                    });
            org.setRegistryNumber(req.registryNumber());
        }

        if (Objects.nonNull(req.contactEmail())) org.setContactEmail(req.contactEmail());
        if (Objects.nonNull(req.companySize())) org.setCompanySize(req.companySize());
        if (Objects.nonNull(req.yearFounded())) org.setYearFounded(req.yearFounded());

        organizationRepository.save(org);
        log.debug("Organization update completed with {}:",registryNumber);
        return OrganizationDTO.convertFromOrganization(org);
    }

    public void delete(UUID id) throws Exception {
        if (!organizationRepository.existsById(id)) {
            throw new Exception("Organization not found");
        }
        organizationRepository.deleteById(id);
        // not: istersen burada org'a ait membership'leri de temizleyebilirsin:
        // memberRepository.deleteByOrganizationId(id);
    }

    @Transactional(readOnly = true)
    public PageResponse<OrganizationDTO> search(OrganizationSearchRequestDTO req, Pageable pageable) {

        Specification<Organization> byName =
                (req.normalizedName() != null && !req.normalizedName().isBlank())
                        ? (root, cq, cb) ->
                        cb.like(root.get("normalizedName"), "%" + req.normalizedName() + "%")
                        : null;

        Specification<Organization> byYear =
                (req.yearFounded() != null)
                        ? (root, cq, cb) -> cb.equal(root.get("yearFounded"), req.yearFounded())
                        : null;

        Specification<Organization> bySize =
                (req.companySize() != null)
                        ? (root, cq, cb) -> cb.equal(root.get("companySize"), req.companySize())
                        : null;

        Specification<Organization> spec = Specification.allOf(byName, byYear, bySize);

        Page<Organization> page = organizationRepository.findAll(spec, pageable);
        Page<OrganizationDTO> dtoPage = page.map(OrganizationDTO::convertFromOrganization);
        return PageResponse.from(dtoPage);
    }

   /* @Transactional(readOnly = true)
    public Page<UUID> listMemberUserIds(UUID organizationId, Pageable pageable) {
        // org var mı?
        if (!organizationRepository.existsById(organizationId)) {
            throw new jakarta.persistence.EntityNotFoundException("Organization not found");
        }
        return OrganizationMemberRepository.findByOrganizationId(organizationId, pageable)
                .map(OrganizationMember::getUserId);
    }*/


}
