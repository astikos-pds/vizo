package br.app.vizo.controller.response;

import br.app.vizo.domain.department.DepartmentRole;

import java.time.Instant;
import java.util.UUID;

public record AssignmentDTO(
        UUID id,
        UUID officialId,
        UUID departmentId,
        DepartmentRole roleInDepartment,
        Boolean canUpdateStatus,
        Boolean canViewReports,
        Boolean canApproveOfficials,
        UUID createdById,
        Instant createdAt
) {
}
