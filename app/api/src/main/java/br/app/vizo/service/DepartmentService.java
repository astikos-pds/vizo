package br.app.vizo.service;

import br.app.vizo.controller.request.CreateDepartmentRequestDTO;
import br.app.vizo.dto.DepartmentDTO;
import br.app.vizo.domain.department.Assignment;
import br.app.vizo.domain.department.Department;
import br.app.vizo.domain.department.DepartmentRole;
import br.app.vizo.dto.AffiliatedUserContextDTO;
import br.app.vizo.exception.NotFoundException;
import br.app.vizo.mapper.OldDepartmentMapper;
import br.app.vizo.repository.OldAssignmentRepository;
import br.app.vizo.repository.OldDepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DepartmentService {

    private final OldDepartmentRepository oldDepartmentRepository;
    private final OldDepartmentMapper oldDepartmentMapper;

    private final OldAssignmentRepository oldAssignmentRepository;

    private final OfficialService officialService;

    public DepartmentService(
            OldDepartmentRepository oldDepartmentRepository,
            OldDepartmentMapper oldDepartmentMapper,
            OldAssignmentRepository oldAssignmentRepository,
            OfficialService officialService
    ) {
        this.oldDepartmentRepository = oldDepartmentRepository;
        this.oldDepartmentMapper = oldDepartmentMapper;
        this.oldAssignmentRepository = oldAssignmentRepository;
        this.officialService = officialService;
    }

    public Page<DepartmentDTO> getDepartments(UUID municipalityId, Pageable pageable, Authentication authentication) {
        this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

        return this.oldDepartmentRepository.findAllByMunicipalityId(municipalityId, pageable)
                .map(this.oldDepartmentMapper::toDto);
    }

    public DepartmentDTO getDepartment(UUID municipalityId, UUID departmentId, Authentication authentication) {
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

        Department department = this.oldDepartmentRepository.findById(departmentId).orElseThrow(
                () -> new NotFoundException("Department not found.")
        );

        return this.oldDepartmentMapper.toDto(department);
    }

    @Transactional
    public DepartmentDTO createDepartment(
            UUID municipalityId,
            CreateDepartmentRequestDTO body,
            Authentication authentication
    ) {
        AffiliatedUserContextDTO context = this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

        Department department = new Department();
        department.setName(body.name());
        department.setIconUrl(body.iconUrl());
        department.setColorHex(body.colorHex());
        department.setProblemTypes(body.problemTypes());
        department.setCreator(context.loggedInUser());
        department.setMunicipality(context.municipality());

        Department saved = this.oldDepartmentRepository.save(department);

        Assignment assignment = new Assignment();
        assignment.setDepartment(department);
        assignment.setUser(context.loggedInUser());
        assignment.setAssignor(context.loggedInUser());
        assignment.setRoleInDepartment(DepartmentRole.ADMIN);
        assignment.setCanViewReports(true);
        assignment.setCanUpdateStatus(true);
        assignment.setCanApproveOfficials(true);

        this.oldAssignmentRepository.save(assignment);

        return this.oldDepartmentMapper.toDto(saved);
    }

    @Transactional
    public void deleteDepartment(UUID municipalityId, UUID departmentId, Authentication authentication) {

        this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

        this.oldAssignmentRepository.deleteAllByDepartmentId(departmentId);

        this.oldDepartmentRepository.deleteById(departmentId);
    }
}
