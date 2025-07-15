package br.app.vizo.service;

import br.app.vizo.controller.filter.AffiliationRequestFilter;
import br.app.vizo.controller.request.UpdateAffiliationRequestDTO;
import br.app.vizo.controller.request.CreateDepartmentRequestDTO;
import br.app.vizo.controller.request.UpdateAssignmentRequestDTO;
import br.app.vizo.controller.response.*;
import br.app.vizo.domain.affiliation.AffiliationRequest;
import br.app.vizo.domain.affiliation.AffiliationRequestStatus;
import br.app.vizo.domain.department.Assignment;
import br.app.vizo.domain.department.Department;
import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.user.Official;
import br.app.vizo.exception.http.ForbiddenException;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.*;
import br.app.vizo.repository.*;
import br.app.vizo.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MunicipalityService {

    private final MunicipalityRepository municipalityRepository;
    private final AffiliationRequestRepository affiliationRequestRepository;
    private final OfficialRepository officialRepository;
    private final DepartmentRepository departmentRepository;
    private final AssignmentRepository assignmentRepository;
    private final MunicipalityMapper municipalityMapper;
    private final AffiliationRequestMapper affiliationRequestMapper;
    private final DepartmentMapper departmentMapper;
    private final AssignmentMapper assignmentMapper;

    public MunicipalityService(
            MunicipalityRepository municipalityRepository,
            AffiliationRequestRepository affiliationRequestRepository,
            OfficialRepository officialRepository,
            DepartmentRepository departmentRepository,
            AssignmentRepository assignmentRepository,
            MunicipalityMapper municipalityMapper,
            AffiliationRequestMapper affiliationRequestMapper,
            DepartmentMapper departmentMapper,
            AssignmentMapper assignmentMapper
    ) {
        this.municipalityRepository = municipalityRepository;
        this.affiliationRequestRepository = affiliationRequestRepository;
        this.officialRepository = officialRepository;
        this.departmentRepository = departmentRepository;
        this.assignmentRepository = assignmentRepository;
        this.municipalityMapper = municipalityMapper;
        this.affiliationRequestMapper = affiliationRequestMapper;
        this.departmentMapper = departmentMapper;
        this.assignmentMapper = assignmentMapper;
    }

    public MunicipalityDTO getMunicipalityById(UUID id) {
        Municipality municipality = this.municipalityRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Municipality not found.")
        );

        return this.municipalityMapper.toDto(municipality);
    }

    public MunicipalityDTO getMunicipalityByEmailDomain(String domain) {
        Municipality municipality = this.municipalityRepository.findByEmailDomain(domain).orElseThrow(
                () -> new NotFoundException("Municipality not found.")
        );

        return this.municipalityMapper.toDto(municipality);
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

    public Page<AssignmentDTO> getAssignments(
            UUID municipalityId,
            UUID departmentId,
            Pageable pageable,
            Authentication authentication
    ) {
        this.getAuthorizedCommonContext(municipalityId, authentication);

        this.departmentRepository.findById(departmentId).orElseThrow(
                () -> new NotFoundException("Department not found.")
        );

        return this.assignmentRepository.findAllByDepartmentId(departmentId, pageable)
                .map(this.assignmentMapper::toDto);
    }

    public AssignmentDTO createOrUpdateAssignment(
            UUID municipalityId,
            UUID departmentId,
            UpdateAssignmentRequestDTO body,
            Authentication authentication
    ) {
        OfficialContext officialContext = this.getAuthorizedAdminContext(municipalityId, authentication);

        Department department = this.departmentRepository.findById(departmentId).orElseThrow(
                () -> new NotFoundException("Department not found.")
        );

        Official official = this.officialRepository.findById(body.officialId()).orElseThrow(
                () -> new NotFoundException("Official not found")
        );

        Assignment assignment = this.assignmentRepository
                .findByDepartmentIdAndOfficialId(departmentId, body.officialId())
                .orElseGet(() -> {
                    Assignment newAssignment = new Assignment();
                    newAssignment.setDepartment(department);
                    newAssignment.setOfficial(official);
                    newAssignment.setCreatedBy(officialContext.loggedInOfficial());

                    return newAssignment;
                });

        assignment.setRoleInDepartment(body.role());
        assignment.setCanViewReports(body.canViewReports());
        assignment.setCanUpdateStatus(body.canUpdateStatus());
        assignment.setCanApproveOfficials(body.canApproveOfficials());

        return this.assignmentMapper.toDto(this.assignmentRepository.save(assignment));
    }

    public AssignmentDTO getAssignment(
            UUID municipalityId,
            UUID departmentId,
            UUID assignmentId,
            Authentication authentication
    ) {
        this.getAuthorizedAdminContext(municipalityId, authentication);

        this.departmentRepository.findById(departmentId).orElseThrow(
                () -> new NotFoundException("Department not found.")
        );

        return this.assignmentMapper.toDto(
                this.assignmentRepository.findById(assignmentId).orElseThrow(
                        () -> new NotFoundException("Assignment not found.")
                )
        );
    }

    public Page<AffiliationRequestDTO> getMunicipalityAffiliations(
            UUID municipalityId,
            AffiliationRequestFilter filter,
            Pageable pageable,
            Authentication authentication
    ) {
        this.getAuthorizedOfficialContext(municipalityId, authentication, true);

        if (filter.status() == null) {
            return this.affiliationRequestRepository
                    .findAllByMunicipalityId(municipalityId, pageable)
                    .map(this.affiliationRequestMapper::toDto);
        }

        return this.affiliationRequestRepository
                .findAllByMunicipalityIdAndStatus(municipalityId, filter.status(), pageable)
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
