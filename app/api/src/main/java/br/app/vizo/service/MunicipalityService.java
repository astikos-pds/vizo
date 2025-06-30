package br.app.vizo.service;

import br.app.vizo.controller.request.UpdateAffiliationRequestDTO;
import br.app.vizo.controller.request.CreateDepartmentRequestDTO;
import br.app.vizo.controller.response.AffiliationRequestDTO;
import br.app.vizo.controller.response.DepartmentDTO;
import br.app.vizo.controller.response.OfficialDTO;
import br.app.vizo.domain.affiliation.AffiliationRequest;
import br.app.vizo.domain.affiliation.AffiliationRequestStatus;
import br.app.vizo.domain.department.Department;
import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.user.Official;
import br.app.vizo.exception.http.ForbiddenException;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.AffiliationRequestMapper;
import br.app.vizo.mapper.DepartmentMapper;
import br.app.vizo.mapper.OfficialMapper;
import br.app.vizo.repository.AffiliationRequestRepository;
import br.app.vizo.repository.DepartmentRepository;
import br.app.vizo.repository.MunicipalityRepository;
import br.app.vizo.repository.OfficialRepository;
import br.app.vizo.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MunicipalityService {

    private final MunicipalityRepository municipalityRepository;
    private final AffiliationRequestRepository affiliationRequestRepository;
    private final OfficialRepository officialRepository;
    private final DepartmentRepository departmentRepository;
    private final AffiliationRequestMapper affiliationRequestMapper;
    private final OfficialMapper officialMapper;
    private final DepartmentMapper departmentMapper;

    public MunicipalityService(
            MunicipalityRepository municipalityRepository,
            AffiliationRequestRepository affiliationRequestRepository,
            OfficialRepository officialRepository,
            DepartmentRepository departmentRepository,
            AffiliationRequestMapper affiliationRequestMapper,
            OfficialMapper officialMapper,
            DepartmentMapper departmentMapper
    ) {
        this.municipalityRepository = municipalityRepository;
        this.affiliationRequestRepository = affiliationRequestRepository;
        this.officialRepository = officialRepository;
        this.departmentRepository = departmentRepository;
        this.affiliationRequestMapper = affiliationRequestMapper;
        this.officialMapper = officialMapper;
        this.departmentMapper = departmentMapper;
    }

    public Page<OfficialDTO> getOfficials(UUID municipalityId, Pageable pageable, Authentication authentication) {
        this.getAuthorizedAdminContext(municipalityId, authentication);

        return this.affiliationRequestRepository
                .findAllByMunicipalityIdAndStatus(municipalityId, AffiliationRequestStatus.APPROVED, pageable)
                .map(AffiliationRequest::getOfficial)
                .map(this.officialMapper::toDto);
    }

    public Page<DepartmentDTO> getDepartments(UUID municipalityId, Pageable pageable, Authentication authentication) {
        this.getAuthorizedCommonContext(municipalityId, authentication);

        return this.departmentRepository
                .findByMunicipalityId(municipalityId,
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                pageable.getSortOr(Sort.by(Sort.Direction.DESC, "createdAt"))
                        )
                )
                .map(this.departmentMapper::toDto);
    }

    public DepartmentDTO createMunicipalityDepartment(
            UUID municipalityId,
            CreateDepartmentRequestDTO body,
            Authentication authentication
    ) {

        OfficialContext officialContext = this.getAuthorizedAdminContext(municipalityId, authentication);

        Department department = new Department();
        department.setName(body.name());
        department.setIconUrl(body.iconUrl());
        department.setColorHex(body.colorHex());
        department.setCreatedBy(officialContext.loggedInOfficial());
        department.setMunicipality(officialContext.municipality());

        return this.departmentMapper.toDto(this.departmentRepository.save(department));
    }

    public Page<AffiliationRequestDTO> getMunicipalityAffiliations(
            UUID municipalityId,
            Pageable pageable,
            Authentication authentication
    ) {
        this.getAuthorizedOfficialContext(municipalityId, authentication, true);

        return this.affiliationRequestRepository
                .findAllByMunicipalityId(municipalityId, pageable)
                .map(this.affiliationRequestMapper::toDto);
    }

    public AffiliationRequestDTO updateMunicipalityAffiliation(
            UUID municipalityId,
            String affiliationId,
            UpdateAffiliationRequestDTO body,
            Authentication authentication
    ) {
        Official loggedInOfficial = this.getAuthorizedAdminContext(municipalityId, authentication).loggedInOfficial();

        AffiliationRequest affiliationRequest = this.affiliationRequestRepository
                .findById(UUID.fromString(affiliationId))
                .orElseThrow(() -> new NotFoundException("Affiliation request not found.")
        );

        affiliationRequest.setStatus(body.status());
        affiliationRequest.setApprovedBy(loggedInOfficial);
        affiliationRequest.setApprovedAt(DateUtil.now());

        if (body.status() == AffiliationRequestStatus.APPROVED) {
            Official official = affiliationRequest.getOfficial();
            official.setWasApproved(true);
            this.officialRepository.save(official);
        }

        return this.affiliationRequestMapper.toDto(this.affiliationRequestRepository.save(affiliationRequest));
    }

    private record OfficialContext(
            Municipality municipality,
            Official loggedInOfficial
    ) {}

    private OfficialContext getAuthorizedCommonContext(UUID municipalityId, Authentication authentication) {
        return getAuthorizedOfficialContext(municipalityId, authentication, false);
    }

    private OfficialContext getAuthorizedAdminContext(UUID municipalityId, Authentication authentication) {
        return getAuthorizedOfficialContext(municipalityId, authentication, true);
    }

    private OfficialContext getAuthorizedOfficialContext(
            UUID municipalityId,
            Authentication authentication,
            boolean isForAdmins
    ) {
        Municipality municipality = this.municipalityRepository.findById(municipalityId).orElseThrow(
                () -> new NotFoundException("Municipality not found.")
        );

        Official loggedInOfficial = this.officialRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("Official not found.")
        );

        validateOfficialAccess(municipality, loggedInOfficial, isForAdmins);

        return new OfficialContext(municipality, loggedInOfficial);
    }

    private void validateOfficialAccess(Municipality municipality, Official official, boolean isForAdmins) {
        if (!official.isAdmin()) {
            boolean officialBelongsToMunicipality = this.affiliationRequestRepository
                    .existsByMunicipalityIdAndOfficialIdAndStatus(
                            municipality.getId(),
                            official.getId(),
                            AffiliationRequestStatus.APPROVED
                    );

            if (!officialBelongsToMunicipality) {
                throw new ForbiddenException("Official does not belong to this municipality.");
            }
        }

        if (isForAdmins && !official.isAdmin()) {
            throw new ForbiddenException("Only admins are allowed to perform this action.");
        }
    }
}
