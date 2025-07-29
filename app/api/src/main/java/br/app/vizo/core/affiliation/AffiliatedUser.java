package br.app.vizo.core.affiliation;

import br.app.vizo.core.affiliation.exception.ForbiddenActionException;
import br.app.vizo.core.affiliation.exception.InvalidPromotionException;
import br.app.vizo.core.affiliation.exception.SelfActionNotAllowedException;
import br.app.vizo.core.assignment.*;
import br.app.vizo.core.department.Department;
import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.problem.ProblemType;
import br.app.vizo.core.shared.Email;
import br.app.vizo.core.user.User;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class AffiliatedUser {

    private final UUID id;
    private final User user;
    private final Municipality municipality;
    private final Email institutionalEmail;
    private boolean isAdmin;
    private AffiliationStatus status;
    private final Instant affiliatedAt;
    private AffiliatedUser approver;
    private Instant approvedAt;

    public AffiliatedUser(UUID id, User user, Municipality municipality, String institutionalEmail, boolean isAdmin, AffiliationStatus status, Instant affiliatedAt, AffiliatedUser approver, Instant approvedAt) {
        this.id = id;
        this.user = user;
        this.municipality = municipality;
        this.institutionalEmail = new Email(institutionalEmail);
        this.isAdmin = isAdmin;
        this.status = status;
        this.affiliatedAt = affiliatedAt;
        this.approver = approver;
        this.approvedAt = approvedAt;
    }

    public AffiliatedUser(User user, Municipality municipality, Email institutionalEmail) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.municipality = municipality;
        this.institutionalEmail = institutionalEmail;
        this.isAdmin = false;
        this.status = AffiliationStatus.PENDING;
        this.affiliatedAt = Instant.now();
        this.approver = null;
        this.approvedAt = null;
    }

    public void promote(AffiliatedUser target) {
        throwIfSameAs(target);
        throwIfNotAdmin();

        if (!target.isApproved()) {
            throw new InvalidPromotionException();
        }

        target.isAdmin = true;
    }

    public void updateStatusOf(AffiliatedUser target, AffiliationStatus newStatus) {
        throwIfSameAs(target);
        throwIfNotAdmin();

        if (target.status == newStatus) return;

        if (newStatus == AffiliationStatus.APPROVED) {
            target.approver = this;
            target.approvedAt = Instant.now();
        }
        target.status = newStatus;
    }

    public Department createDepartment(String name, String colorHex, String iconUrl, Set<ProblemType> problemTypes) {
        this.throwIfNotAdmin();

        return new Department(this.municipality, this, name, colorHex, iconUrl, problemTypes);
    }

    public AssignmentIntent assign(AffiliatedUser target) {
        throwIfSameAs(target);
        throwIfNotAdmin();

        return new AssignmentIntent(target, Permission.common());
    }

    public AssignmentIntent assignSelf() {
        throwIfNotAdmin();

        return new AssignmentIntent(this, Permission.admin());
    }

    public PermissionPreset createPermissionPreset(String name, Permission permission) {
        return new PermissionPreset(this.municipality, name, permission);
    }

    public boolean isApproved() {
        return status == AffiliationStatus.APPROVED;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public String getInstitutionalEmail() {
        return institutionalEmail.value();
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public AffiliationStatus getStatus() {
        return status;
    }

    public Instant getAffiliatedAt() {
        return affiliatedAt;
    }

    public AffiliatedUser getApprover() {
        return approver;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    private boolean isSameAs(AffiliatedUser other) {
        return this.id.equals(other.getId());
    }

    private void throwIfSameAs(AffiliatedUser other) {
        if (this.isSameAs(other)) {
            throw new SelfActionNotAllowedException();
        }
    }

    private void throwIfNotAdmin() {
        if (!this.isAdmin) {
            throw new ForbiddenActionException("You need to be an admin to execute this action.");
        }
    }
}
