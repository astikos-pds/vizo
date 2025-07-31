package br.app.vizo.application.dto;

public record PermissionDTO(
        boolean canViewReports,
        boolean canUpdateStatus,
        boolean canManageUsers
) {
}
