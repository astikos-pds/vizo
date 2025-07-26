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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AffiliatedUser {

    @Getter private final UUID id;
    @Getter private final User user;
    @Getter private final Municipality municipality;
    @Getter private final Email institutionalEmail;
    private boolean isAdmin;
    private AffiliationStatus status;
    @Getter private final Instant affiliatedAt;
    @Getter private AffiliatedUser approver;
    @Getter private Instant approvedAt;

    public AffiliatedUser(User user, Municipality municipality, Email institutionalEmail) {
        this(
                UUID.randomUUID(),
                user,
                municipality,
                institutionalEmail,
                false,
                AffiliationStatus.PENDING,
                Instant.now(),
                null,
                null
        );
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
