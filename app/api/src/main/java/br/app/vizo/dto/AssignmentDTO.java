package br.app.vizo.dto;

import br.app.vizo.domain.department.DepartmentRole;
import br.app.vizo.domain.user.User;

import java.time.Instant;
import java.util.UUID;

public record AssignmentDTO(
        UUID id,
        UserDTO user,
        DepartmentDTO department,
        DepartmentRole roleInDepartment,
        Boolean canUpdateStatus,
        Boolean canViewReports,
        Boolean canApproveOfficials,
        User assignor,
        Instant createdAt
) {
}
