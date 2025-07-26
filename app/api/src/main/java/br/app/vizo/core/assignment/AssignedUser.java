package br.app.vizo.core.assignment;

import br.app.vizo.core.affiliation.AffiliatedUser;
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
        this(user, department, null, new Permission());
    }

    public AssignedUser(AffiliatedUser user, Department department, PermissionPreset permissionPreset) {
        this(user, department, permissionPreset, null);
    }

    public void update(PermissionMode permissionMode, PermissionPreset permissionPreset, Permission permission) {
        this.permissionMode = permissionMode;
        this.permissionPreset = permissionPreset;
        this.customPermission = permission;
    }
}
