package br.app.vizo.application.usecase.assignment;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.AssignedUserDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.AssignedUserMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.core.assignment.AssignedUserRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetUsersAssignedToDepartmentUseCase {

    private final AuthorizationService authorizationService;
    private final AssignedUserRepository assignedUserRepository;
    private final AssignedUserMapper assignedUserMapper;

    public PageDTO<AssignedUserDTO> execute(User loggedInUser, UUID municipalityId, UUID departmentId, PaginationDTO pagination) {
        this.authorizationService.ensureUserIsAssignedTo(loggedInUser, municipalityId, departmentId);

        return this.assignedUserRepository.findAllByDepartmentId(departmentId, pagination)
                .map(this.assignedUserMapper::toDto);
    }
}
