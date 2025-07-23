package br.app.vizo.service;

import br.app.vizo.controller.request.BatchUpdateAssignmentRequestDTO;
import br.app.vizo.controller.request.UpdateAssignmentRequestDTO;
import br.app.vizo.dto.AssignmentDTO;
import br.app.vizo.domain.department.Assignment;
import br.app.vizo.domain.department.Department;
import br.app.vizo.domain.department.DepartmentRole;
import br.app.vizo.domain.user.Official;
import br.app.vizo.dto.OfficialContextDTO;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.AssignmentMapper;
import br.app.vizo.repository.AssignmentRepository;
import br.app.vizo.repository.DepartmentRepository;
import br.app.vizo.repository.OfficialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentMapper assignmentMapper;

    private final DepartmentRepository departmentRepository;

    private final OfficialRepository officialRepository;

    private final OfficialService officialService;

    public AssignmentService(
            AssignmentRepository assignmentRepository,
            AssignmentMapper assignmentMapper,
            DepartmentRepository departmentRepository,
            OfficialRepository officialRepository,
            OfficialService officialService
    ) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentMapper = assignmentMapper;
        this.departmentRepository = departmentRepository;
        this.officialRepository = officialRepository;
        this.officialService = officialService;
    }

    public Page<AssignmentDTO> getAssignments(
            UUID municipalityId,
            UUID departmentId,
            Pageable pageable,
            Authentication authentication
    ) {
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

        this.getDepartment(departmentId);

        return this.assignmentRepository.findAllByDepartmentId(departmentId, pageable)
                .map(this.assignmentMapper::toDto);
    }

    public AssignmentDTO getAssignment(
            UUID municipalityId,
            UUID departmentId,
            UUID assignmentId,
            Authentication authentication
    ) {
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

        this.getDepartment(departmentId);

        Assignment assignment = this.assignmentRepository.findById(assignmentId).orElseThrow(
                () -> new NotFoundException("Assignment not found.")
        );

        return this.assignmentMapper.toDto(assignment);
    }

    public AssignmentDTO createOrUpdateAssignment(
            UUID municipalityId,
            UUID departmentId,
            UpdateAssignmentRequestDTO body,
            Authentication authentication
    ) {
        OfficialContextDTO context = this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

        Department department = this.departmentRepository.findById(departmentId).orElseThrow(
                () -> new NotFoundException("Department not found.")
        );

        Assignment assignment = this.assignmentRepository
                .findByDepartmentIdAndOfficialId(departmentId, body.officialId())
                .orElseGet(() -> {
                    Assignment newAssignment = new Assignment();
                    newAssignment.setDepartment(department);
                    newAssignment.setOfficial(context.loggedInOfficial());
                    newAssignment.setCreatedBy(context.loggedInOfficial());

                    return newAssignment;
                });

        assignment.setRoleInDepartment(body.role());
        assignment.setCanViewReports(body.canViewReports());
        assignment.setCanUpdateStatus(body.canUpdateStatus());
        assignment.setCanApproveOfficials(body.canApproveOfficials());

        Assignment saved = this.assignmentRepository.save(assignment);

        return this.assignmentMapper.toDto(saved);
    }

    @Transactional
    public List<AssignmentDTO> createOrUpdateAssignmentInBatch(
            UUID municipalityId,
            UUID departmentId,
            BatchUpdateAssignmentRequestDTO body,
            Authentication authentication
    ) {
        OfficialContextDTO context = this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

        Department department = this.getDepartment(departmentId);

        List<Assignment> assignments = new ArrayList<>();

        for (UUID id : body.ids()) {
            Official official = this.officialRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Official not found.")
            );

            Assignment assignment = this.assignmentRepository
                    .findByDepartmentIdAndOfficialId(departmentId, id)
                    .orElseGet(() -> {
                        Assignment newAssignment = new Assignment();
                        newAssignment.setDepartment(department);
                        newAssignment.setOfficial(official);
                        newAssignment.setCreatedBy(context.loggedInOfficial());
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

    public void deleteAssignment(
            UUID municipalityId,
            UUID departmentId,
            UUID assignmentId,
            Authentication authentication
    ) {
        this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

        this.getDepartment(departmentId);

        this.assignmentRepository.deleteById(assignmentId);
    }

    private Department getDepartment(UUID id) {
        return this.departmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Department not found.")
        );
    }
}
