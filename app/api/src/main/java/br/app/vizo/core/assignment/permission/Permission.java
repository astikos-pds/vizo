package br.app.vizo.core.assignment.permission;

public record Permission(
        boolean canViewReports,
        boolean canUpdateStatus,
        boolean canManageUsers
) {

    public static Permission common() {
        return new Permission(true, false, false);
    }

    public static Permission admin() {
        return new Permission(true, true, true);
    }
}
