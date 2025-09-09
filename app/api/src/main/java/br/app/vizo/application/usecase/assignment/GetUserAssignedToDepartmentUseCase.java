package br.app.vizo.application.usecase.assignment;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.AssignedUserDTO;
import br.app.vizo.application.exception.AssignmentRequiredException;
import br.app.vizo.application.mapper.AssignedUserMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.core.assignment.AssignedUserRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetUserAssignedToDepartmentUseCase {

    private final AuthorizationService authorizationService;
    private final AssignedUserRepository assignedUserRepository;
    private final AssignedUserMapper assignedUserMapper;

    public AssignedUserDTO execute(User loggedInUser, UUID municipalityId, UUID departmentId, UUID assignmentId) {
        this.authorizationService.ensureUserIsAssignedTo(loggedInUser, municipalityId, departmentId);

        return this.assignedUserRepository.findById(assignmentId)
                .map(this.assignedUserMapper::toDto)
                .orElseThrow(AssignmentRequiredException::new);
    }
}
