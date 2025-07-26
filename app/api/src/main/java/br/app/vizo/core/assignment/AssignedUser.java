package br.app.vizo.core.assignment;

import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.affiliation.exception.ForbiddenActionException;
import br.app.vizo.core.affiliation.exception.SelfActionNotAllowedException;
import br.app.vizo.core.assignment.exception.InvalidAssignedUserException;
import br.app.vizo.core.department.Department;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AssignedUser {

    private final UUID id;
    private final AffiliatedUser user;
    private final Department department;
    private PermissionMode permissionMode;
    private PermissionPreset permissionPreset;
    private Permission customPermission;
    private final Instant assignedAt;

    private AssignedUser(
            AffiliatedUser user,
            Department department,
            PermissionPreset permissionPreset,
            Permission permission
    ) {
        if (user == null || department == null || (permissionPreset == null && permission == null)) {
            throw new InvalidAssignedUserException();
        }

        this.id = UUID.randomUUID();
        this.user = user;
        this.department = department;
        this.permissionMode = permissionPreset == null ? PermissionMode.CUSTOM : PermissionMode.PRESET;
        this.permissionPreset = permissionPreset;
        this.customPermission = permission;
        this.assignedAt = Instant.now();
    }

    public AssignedUser(AffiliatedUser user, Department department) {
        this(user, department, null, Permission.common());
    }

    public AssignedUser(AffiliatedUser user, Department department, Permission permission) {
        this(user, department, null, permission);
    }

    public void updatePermission(AssignedUser target, PermissionMode mode) {
        throwIfSameAs(target);
        throwIfNotAllowed();

        target.permissionMode = mode;
    }

    public void updatePermission(AssignedUser target, PermissionPreset preset) {
        throwIfSameAs(target);
        throwIfNotAllowed();

        target.permissionMode = PermissionMode.PRESET;
        target.permissionPreset = preset;
    }

    public void updatePermission(AssignedUser target, Permission permission) {
        throwIfSameAs(target);
        throwIfNotAllowed();

        target.permissionMode = PermissionMode.CUSTOM;
        target.customPermission = permission;
    }

    public boolean canViewReports() {
        return this.getEffectivePermission().canViewReports();
    }

    public boolean canUpdateStatus() {
        return this.getEffectivePermission().canUpdateStatus();
    }

    public boolean canManageUsers() {
        return this.getEffectivePermission().canManageUsers();
    }

    public void throwIfSameAs(AssignedUser other) {
        if (this.id.equals(other.getId())) {
            throw new SelfActionNotAllowedException();
        }
    }

    public void throwIfNotAllowed() {
        if (!this.canManageUsers()) {
            throw new ForbiddenActionException("You don't have permission to execute this action.");
        }
    }

    private Permission getEffectivePermission() {
        return this.permissionMode == PermissionMode.PRESET
                ? permissionPreset.getPermission()
                : customPermission;
    }
}
