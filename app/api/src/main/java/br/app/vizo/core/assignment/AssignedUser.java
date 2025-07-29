package br.app.vizo.core.assignment;

import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.affiliation.exception.ForbiddenActionException;
import br.app.vizo.core.affiliation.exception.SelfActionNotAllowedException;
import br.app.vizo.core.assignment.exception.InvalidAssignedUserException;
import br.app.vizo.core.assignment.exception.OutOfScopeException;
import br.app.vizo.core.department.Department;
import br.app.vizo.core.problem.Problem;
import br.app.vizo.core.problem.StatusUpdate;
import br.app.vizo.core.problem.ProblemStatus;

import java.time.Instant;
import java.util.UUID;

public class AssignedUser {

    private final UUID id;
    private final AffiliatedUser user;
    private final Department department;
    private PermissionMode permissionMode;
    private PermissionPreset permissionPreset;
    private Permission customPermission;
    private final Instant assignedAt;

    public AssignedUser(UUID id, AffiliatedUser user, Department department, PermissionMode permissionMode, PermissionPreset permissionPreset, Permission customPermission, Instant assignedAt) {
        this.id = id;
        this.user = user;
        this.department = department;
        this.permissionMode = permissionMode;
        this.permissionPreset = permissionPreset;
        this.customPermission = customPermission;
        this.assignedAt = assignedAt;
    }

    AssignedUser(AffiliatedUser user, Department department) {
        if (user == null || department == null) {
            throw new InvalidAssignedUserException();
        }

        this.id = UUID.randomUUID();
        this.user = user;
        this.department = department;
        this.assignedAt = Instant.now();
    }

    public AssignedUser withCustomPermission(Permission permission) {
        this.permissionMode = PermissionMode.CUSTOM;
        this.customPermission = permission;

        return this;
    }

    public StatusUpdate updateProblemStatus(Problem problem, ProblemStatus status, String text) {
        throwIfProblemIsOutOfScope(problem);
        throwIfCannotUpdateStatus();

        problem.updateStatusTo(status);
        return new StatusUpdate(problem, text, status);
    }

    public void updatePermissionOf(AssignedUser target, PermissionMode mode) {
        throwIfSameAs(target);
        throwIfCannotManageUsers();

        target.permissionMode = mode;
    }

    public void updatePermissionOf(AssignedUser target, PermissionPreset preset) {
        throwIfSameAs(target);
        throwIfCannotManageUsers();

        target.permissionMode = PermissionMode.PRESET;
        target.permissionPreset = preset;
    }

    public void updatePermissionOf(AssignedUser target, Permission permission) {
        throwIfSameAs(target);
        throwIfCannotManageUsers();

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

    public UUID getId() {
        return id;
    }

    public AffiliatedUser getUser() {
        return user;
    }

    public Department getDepartment() {
        return department;
    }

    public PermissionMode getPermissionMode() {
        return permissionMode;
    }

    public PermissionPreset getPermissionPreset() {
        return permissionPreset;
    }

    public Permission getCustomPermission() {
        return customPermission;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    private void throwIfSameAs(AssignedUser other) {
        if (this.id.equals(other.getId())) {
            throw new SelfActionNotAllowedException();
        }
    }

    private void throwIfProblemIsOutOfScope(Problem problem) {
        if (!this.department.isResponsibleBy(problem)) {
            throw new OutOfScopeException();
        }
    }

    private void throwIfCannotUpdateStatus() {
        throwIfNotAllowed(this.canUpdateStatus());
    }

    private void throwIfCannotManageUsers() {
        throwIfNotAllowed(this.canManageUsers());
    }

    private void throwIfNotAllowed(boolean canDoAction) {
        if (!canDoAction) {
            throw new ForbiddenActionException("You don't have permission to execute this action.");
        }
    }

    private Permission getEffectivePermission() {
        return this.permissionMode == PermissionMode.PRESET
                ? permissionPreset.getPermission()
                : customPermission;
    }
}
