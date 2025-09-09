package br.app.vizo.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;

@Embeddable
public record PermissionEntity(
        @Column(name = "can_view_reports", nullable = false)
        boolean canViewReports,

        @Column(name = "can_update_status", nullable = false)
        boolean canUpdateStatus,

        @Column(name = "can_manage_users", nullable = false)
        boolean canManageUsers
) {
}
