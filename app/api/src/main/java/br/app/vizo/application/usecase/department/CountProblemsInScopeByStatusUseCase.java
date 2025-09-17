package br.app.vizo.application.usecase.department;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.application.usecase.department.param.CountProblemsInScopeParams;
import br.app.vizo.core.assignment.AssignedUser;
import br.app.vizo.core.problem.ProblemRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class CountProblemsInScopeByStatusUseCase {

    private final ProblemRepository problemRepository;
    private final AuthorizationService authorizationService;

    public long execute(User loggedInUser, UUID municipalityId, UUID departmentId, CountProblemsInScopeParams params) {
        AssignedUser assignedUser = this.authorizationService
                .ensureUserIsAssignedTo(loggedInUser, municipalityId, departmentId);

        return this.problemRepository.countByStatusAndTypeIn(params.status(), assignedUser.getDepartment().getScope());
    }
}
