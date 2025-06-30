package br.app.vizo.controller.request;

import br.app.vizo.domain.department.DepartmentRole;

import java.util.UUID;

public record UpdateAssignmentRequestDTO(
        UUID officialId,
        DepartmentRole role,
        boolean canUpdateStatus,
        boolean canViewReports,
        boolean canApproveOfficials
) {
}
