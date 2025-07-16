package br.app.vizo.service;

import br.app.vizo.controller.filter.AffiliationRequestFilter;
import br.app.vizo.controller.request.BatchUpdateAssignmentRequestDTO;
import br.app.vizo.controller.request.UpdateAffiliationRequestDTO;
import br.app.vizo.controller.request.CreateDepartmentRequestDTO;
import br.app.vizo.controller.request.UpdateAssignmentRequestDTO;
import br.app.vizo.controller.response.*;
import br.app.vizo.domain.affiliation.AffiliationRequest;
import br.app.vizo.domain.affiliation.AffiliationRequestStatus;
import br.app.vizo.domain.department.Assignment;
import br.app.vizo.domain.department.Department;
import br.app.vizo.domain.department.DepartmentRole;
import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.user.Official;
import br.app.vizo.dto.OfficialContextDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MunicipalityService {

    private final OfficialService officialService;
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
            OfficialService officialService,
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
        this.officialService = officialService;
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
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

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

    @Transactional
    public DepartmentDTO createMunicipalityDepartment(
            UUID municipalityId,
            CreateDepartmentRequestDTO body,
            Authentication authentication
    ) {
        OfficialContextDTO officialContext = this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

        Department department = new Department();
        department.setName(body.name());
        department.setIconUrl(body.iconUrl());
        department.setColorHex(body.colorHex());
        department.setCreatedBy(officialContext.loggedInOfficial());
        department.setMunicipality(officialContext.municipality());

        department = this.departmentRepository.save(department);

        Assignment assignment = new Assignment();
        assignment.setDepartment(department);
        assignment.setOfficial(officialContext.loggedInOfficial());
        assignment.setCreatedBy(officialContext.loggedInOfficial());
        assignment.setRoleInDepartment(DepartmentRole.ADMIN);
        assignment.setCanViewReports(true);
        assignment.setCanUpdateStatus(true);
        assignment.setCanApproveOfficials(true);

        this.assignmentRepository.save(assignment);

        return this.departmentMapper.toDto(department);
    }

    @Transactional
    public void deleteDepartment(UUID municipalityId, UUID departmentId, Authentication authentication) {
        this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

        this.assignmentRepository.deleteAllByDepartmentId(departmentId);

        this.departmentRepository.deleteById(departmentId);
    }

    public Page<AssignmentDTO> getAssignments(
            UUID municipalityId,
            UUID departmentId,
            Pageable pageable,
            Authentication authentication
    ) {
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

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
        OfficialContextDTO officialContext = this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

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

    @Transactional
    public List<AssignmentDTO> createOrUpdateAssignmentInBatch(
            UUID municipalityId,
            UUID departmentId,
            BatchUpdateAssignmentRequestDTO body,
            Authentication authentication
    ) {
        OfficialContextDTO officialContext = this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

        Department department = this.departmentRepository.findById(departmentId).orElseThrow(
                () -> new NotFoundException("Department not found.")
        );

        List<Assignment> assignments = new ArrayList<>();

        for (UUID id : body.ids()) {
            Official official = this.officialRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Official not found")
            );

            Assignment assignment = this.assignmentRepository
                    .findByDepartmentIdAndOfficialId(departmentId, id)
                    .orElseGet(() -> {
                        Assignment newAssignment = new Assignment();
                        newAssignment.setDepartment(department);
                        newAssignment.setOfficial(official);
                        newAssignment.setCreatedBy(officialContext.loggedInOfficial());
                        newAssignment.setRoleInDepartment(DepartmentRole.COMMON);
                        newAssignment.setCanViewReports(true);
                        newAssignment.setCanUpdateStatus(true);
                        newAssignment.setCanApproveOfficials(false);

                        return newAssignment;
                    });

            assignments.add(assignment);
        }

        return this.assignmentRepository.saveAll(assignments)
                .stream()
                .map(this.assignmentMapper::toDto)
                .toList();
    }

    public AssignmentDTO getAssignment(
            UUID municipalityId,
            UUID departmentId,
            UUID assignmentId,
            Authentication authentication
    ) {
        this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

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
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

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
        Official loggedInOfficial = this.officialService.getAuthorizedAdminContext(municipalityId, authentication).loggedInOfficial();

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
}
