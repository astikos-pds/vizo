package br.app.vizo.application.usecase.assignment;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.exception.AssignmentNotFoundException;
import br.app.vizo.application.exception.PermissionDeniedException;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.core.affiliation.AffiliatedUserRepository;
import br.app.vizo.core.affiliation.exception.SelfActionNotAllowedException;
import br.app.vizo.core.assignment.AssignedUser;
import br.app.vizo.core.assignment.AssignedUserRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class RemoveAssigneeFromDepartmentUseCase {

    private final AuthorizationService authorizationService;
    private final AssignedUserRepository assignedUserRepository;

    public void execute(User loggedInUser, UUID municipalityId, UUID departmentId, UUID assignmentId) {
        AssignedUser assignedUser = this.authorizationService.ensureUserIsAssignedTo(loggedInUser, municipalityId, departmentId);

        if (!assignedUser.canManageUsers()) {
            throw new PermissionDeniedException();
        }

        AssignedUser targetAssignedUser = this.assignedUserRepository.findById(assignmentId)
                .orElseThrow(AssignmentNotFoundException::new);

        if (assignedUser.isSameAs(targetAssignedUser)) {
            throw new SelfActionNotAllowedException();
        }

        this.assignedUserRepository.deleteById(assignmentId);
    }

}
