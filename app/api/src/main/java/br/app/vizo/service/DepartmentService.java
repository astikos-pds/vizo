package br.app.vizo.service;

import br.app.vizo.controller.request.CreateDepartmentRequestDTO;
import br.app.vizo.controller.response.DepartmentDTO;
import br.app.vizo.domain.department.Assignment;
import br.app.vizo.domain.department.Department;
import br.app.vizo.domain.department.DepartmentRole;
import br.app.vizo.dto.OfficialContextDTO;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.DepartmentMapper;
import br.app.vizo.repository.AssignmentRepository;
import br.app.vizo.repository.DepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    private final AssignmentRepository assignmentRepository;

    private final OfficialService officialService;

    public DepartmentService(
            DepartmentRepository departmentRepository,
            DepartmentMapper departmentMapper,
            AssignmentRepository assignmentRepository,
            OfficialService officialService
    ) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.assignmentRepository = assignmentRepository;
        this.officialService = officialService;
    }

    public Page<DepartmentDTO> getDepartments(UUID municipalityId, Pageable pageable, Authentication authentication) {
        this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

        return this.departmentRepository.findAllByMunicipalityId(municipalityId, pageable)
                .map(this.departmentMapper::toDto);
    }

    public DepartmentDTO getDepartment(UUID municipalityId, UUID departmentId, Authentication authentication) {
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

        Department department = this.departmentRepository.findById(departmentId).orElseThrow(
                () -> new NotFoundException("Department not found.")
        );

        return this.departmentMapper.toDto(department);
    }

    @Transactional
    public DepartmentDTO createDepartment(
            UUID municipalityId,
            CreateDepartmentRequestDTO body,
            Authentication authentication
    ) {
        OfficialContextDTO context = this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

        Department department = new Department();
        department.setName(body.name());
        department.setIconUrl(body.iconUrl());
        department.setColorHex(body.colorHex());
        department.setProblemTypes(body.problemTypes());
        department.setCreatedBy(context.loggedInOfficial());
        department.setMunicipality(context.municipality());

        Department saved = this.departmentRepository.save(department);

        Assignment assignment = new Assignment();
        assignment.setDepartment(department);
        assignment.setOfficial(context.loggedInOfficial());
        assignment.setCreatedBy(context.loggedInOfficial());
        assignment.setRoleInDepartment(DepartmentRole.ADMIN);
        assignment.setCanViewReports(true);
        assignment.setCanUpdateStatus(true);
        assignment.setCanApproveOfficials(true);

        this.assignmentRepository.save(assignment);

        return this.departmentMapper.toDto(saved);
    }

    @Transactional
    public void deleteDepartment(UUID municipalityId, UUID departmentId, Authentication authentication) {

        this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

        this.assignmentRepository.deleteAllByDepartmentId(departmentId);

        this.departmentRepository.deleteById(departmentId);
    }
}
