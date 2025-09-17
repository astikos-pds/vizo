package br.app.vizo.application.usecase.department;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.core.assignment.AssignedUser;
import br.app.vizo.core.problem.ProblemCountByStatus;
import br.app.vizo.core.problem.ProblemRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class CountProblemsInScopeByStatusUseCase {

    private final ProblemRepository problemRepository;
    private final AuthorizationService authorizationService;

    public List<ProblemCountByStatus> execute(User loggedInUser, UUID municipalityId, UUID departmentId) {
        AssignedUser assignedUser = this.authorizationService
                .ensureUserIsAssignedTo(loggedInUser, municipalityId, departmentId);

        return this.problemRepository.countByStatusAndTypeIn(assignedUser.getDepartment().getScope());
    }
}
