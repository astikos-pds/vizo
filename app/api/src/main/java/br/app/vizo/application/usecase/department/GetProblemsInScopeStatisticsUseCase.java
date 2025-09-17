package br.app.vizo.application.usecase.department;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.application.usecase.department.param.GetProblemsInScopeStatisticsParams;
import br.app.vizo.core.assignment.AssignedUser;
import br.app.vizo.core.problem.ProblemRepository;
import br.app.vizo.core.problem.ProblemStatistics;
import br.app.vizo.core.problem.ProblemStatus;
import br.app.vizo.core.problem.ProblemType;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class GetProblemsInScopeStatisticsUseCase {

    private final AuthorizationService authorizationService;
    private final ProblemRepository problemRepository;

    public List<ProblemStatistics> execute(
            User loggedInUser,
            UUID municipalityId,
            UUID departmentId,
            GetProblemsInScopeStatisticsParams params
    ) {
        AssignedUser assignedUser = this.authorizationService
                .ensureUserIsAssignedTo(loggedInUser, municipalityId, departmentId);

        Set<ProblemStatus> statuses;
        if (params.statuses() == null || params.statuses().isEmpty()) {
            statuses = Arrays.stream(ProblemStatus.values()).collect(Collectors.toSet());
        } else {
            statuses = params.statuses();
        }

        Set<ProblemType> types;
        if (params.types() == null || params.types().isEmpty()) {
            types = assignedUser.getDepartment().getScope();
        } else {
            types = params.types();
        }

        return this.problemRepository.countByRangeAndStatusesAndTypes(params.start(), params.end(), statuses, types);
    }
}
