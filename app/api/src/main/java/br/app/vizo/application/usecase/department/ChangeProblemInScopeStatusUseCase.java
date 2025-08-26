package br.app.vizo.application.usecase.department;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.ProblemDTO;
import br.app.vizo.application.exception.ProblemNotFoundException;
import br.app.vizo.application.exception.ProblemOutOfScopeException;
import br.app.vizo.application.mapper.ProblemMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.application.usecase.department.request.ChangeProblemStatusRequestDTO;
import br.app.vizo.core.assignment.AssignedUser;
import br.app.vizo.core.department.Department;
import br.app.vizo.core.problem.Problem;
import br.app.vizo.core.problem.ProblemRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class ChangeProblemInScopeStatusUseCase {

    private final AuthorizationService authorizationService;
    private final ProblemRepository problemRepository;
    private final ProblemMapper problemMapper;

    public ProblemDTO execute(
            User loggedInUser,
            UUID municipalityId,
            UUID departmentId,
            UUID problemId,
            ChangeProblemStatusRequestDTO request
    ) {
        AssignedUser assignedUser = this.authorizationService.ensureUserIsAssignedTo(loggedInUser, municipalityId, departmentId);

        Problem problem = this.problemRepository.findById(problemId).orElseThrow(ProblemNotFoundException::new);
        Department department = assignedUser.getDepartment();

        if (!department.isResponsibleBy(problem)) {
            throw new ProblemOutOfScopeException();
        }

        Problem updated = assignedUser.changeProblemStatus(problem, request.status());

        Problem saved = this.problemRepository.save(updated);
        return this.problemMapper.toDto(saved);
    }
}
