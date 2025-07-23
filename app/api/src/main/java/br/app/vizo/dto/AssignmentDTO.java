package br.app.vizo.dto;

import br.app.vizo.domain.department.DepartmentRole;

import java.time.Instant;
import java.util.UUID;

public record AssignmentDTO(
        UUID id,
        OfficialDTO official,
        DepartmentDTO department,
        DepartmentRole roleInDepartment,
        Boolean canUpdateStatus,
        Boolean canViewReports,
        Boolean canApproveOfficials,
        OfficialDTO createdBy,
        Instant createdAt
) {
}
