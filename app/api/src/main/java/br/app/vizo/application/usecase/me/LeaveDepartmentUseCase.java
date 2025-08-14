package br.app.vizo.application.usecase.me;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.core.assignment.AssignedUser;
import br.app.vizo.core.assignment.AssignedUserRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class LeaveDepartmentUseCase {

    private final AuthorizationService authorizationService;
    private final AssignedUserRepository assignedUserRepository;

    public void execute(User loggedInUser, UUID municipalityId, UUID departmentId) {
        AssignedUser assignedUser = this.authorizationService.ensureUserIsAssignedTo(loggedInUser, municipalityId, departmentId);

        this.assignedUserRepository.deleteById(assignedUser.getId());
    }
}
