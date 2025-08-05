package br.app.vizo.application.usecase.department;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.ProblemDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.ProblemMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.core.assignment.AssignedUser;
import br.app.vizo.core.department.Department;
import br.app.vizo.core.problem.ProblemRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetProblemsInScopeUseCase {

    private final AuthorizationService authorizationService;
    private final ProblemRepository problemRepository;
    private final ProblemMapper problemMapper;

    public PageDTO<ProblemDTO> execute(User loggedInUser, UUID municipalityId, UUID departmentId, PaginationDTO pagination) {
        AssignedUser assignedUser = this.authorizationService.ensureUserIsAssignedTo(loggedInUser, municipalityId, departmentId);
        Department department = assignedUser.getDepartment();

        return this.problemRepository.findAllByTypeIn(department.getScope(), pagination)
                .map(this.problemMapper::toDto);
    }
}
