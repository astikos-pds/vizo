package br.app.vizo.core.assignment;

public record Permission(
        boolean canViewReports,
        boolean canUpdateStatus,
        boolean canManageUsers
) {

    public Permission() {
        this(true, false, false);
    }
}
