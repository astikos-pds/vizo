package br.app.vizo.service;

import br.app.vizo.controller.request.BatchUpdateAssignmentRequestDTO;
import br.app.vizo.controller.request.UpdateAssignmentRequestDTO;
import br.app.vizo.domain.user.User;
import br.app.vizo.dto.AssignmentDTO;
import br.app.vizo.domain.department.Assignment;
import br.app.vizo.domain.department.Department;
import br.app.vizo.domain.department.DepartmentRole;
import br.app.vizo.dto.AffiliatedUserContextDTO;
import br.app.vizo.exception.NotFoundException;
import br.app.vizo.mapper.AssignmentMapper;
import br.app.vizo.repository.AssignmentRepository;
import br.app.vizo.repository.DepartmentRepository;
import br.app.vizo.repository.UserRepository;
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

    private final UserRepository userRepository;

    private final OfficialService officialService;

    public AssignmentService(
            AssignmentRepository assignmentRepository,
            AssignmentMapper assignmentMapper,
            DepartmentRepository departmentRepository,
            UserRepository userRepository,
            OfficialService officialService
    ) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentMapper = assignmentMapper;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
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
        AffiliatedUserContextDTO context = this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

        Department department = this.departmentRepository.findById(departmentId).orElseThrow(
                () -> new NotFoundException("Department not found.")
        );

        Assignment assignment = this.assignmentRepository
                .findByDepartmentIdAndUserId(departmentId, body.officialId())
                .orElseGet(() -> {
                    Assignment newAssignment = new Assignment();
                    newAssignment.setDepartment(department);
                    newAssignment.setUser(context.loggedInUser());
                    newAssignment.setAssignor(context.loggedInUser());

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
        AffiliatedUserContextDTO context = this.officialService.getAuthorizedAdminContext(municipalityId, authentication);

        Department department = this.getDepartment(departmentId);

        List<Assignment> assignments = new ArrayList<>();

        for (UUID id : body.ids()) {
            User user = this.userRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Official not found.")
            );

            Assignment assignment = this.assignmentRepository
                    .findByDepartmentIdAndUserId(departmentId, id)
                    .orElseGet(() -> {
                        Assignment newAssignment = new Assignment();
                        newAssignment.setDepartment(department);
                        newAssignment.setUser(user);
                        newAssignment.setAssignor(context.loggedInUser());
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
