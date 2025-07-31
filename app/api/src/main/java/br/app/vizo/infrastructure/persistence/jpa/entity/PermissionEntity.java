package br.app.vizo.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;

@Embeddable
public record PermissionEntity(
        @Column(name = "can_view_reports", nullable = false, columnDefinition = "DEFAULT TRUE")
        boolean canViewReports,

        @Column(name = "can_update_status", nullable = false, columnDefinition = "DEFAULT FALSE")
        boolean canUpdateStatus,

        @Column(name = "can_manage_users", nullable = false, columnDefinition = "DEFAULT FALSE")
        boolean canManageUsers
) {
}
