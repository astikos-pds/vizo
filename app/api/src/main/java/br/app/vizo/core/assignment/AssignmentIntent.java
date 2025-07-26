package br.app.vizo.core.assignment;

import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.department.Department;

public record AssignmentIntent(
        AffiliatedUser user,
        Permission permission
) {

    public AssignedUser to(Department department) {
        return new AssignedUser(user, department).withCustomPermission(permission);
    }
}
