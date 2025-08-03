package br.app.vizo.application.usecase.assignment;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.AssignedUserDTO;
import br.app.vizo.application.exception.DepartmentNotFoundException;
import br.app.vizo.application.exception.MustBeAdminException;
import br.app.vizo.application.mapper.AssignedUserMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.application.usecase.assignment.request.AssignUsersToDepartmentRequestDTO;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.assignment.AssignedUser;
import br.app.vizo.core.assignment.AssignedUserRepository;
import br.app.vizo.core.department.Department;
import br.app.vizo.core.department.DepartmentRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class AssignUsersToDepartmentUseCase {

    private final AuthorizationService authorizationService;
    private final DepartmentRepository departmentRepository;
    private final AssignedUserRepository assignedUserRepository;
    private final AssignedUserMapper assignedUserMapper;

    public List<AssignedUserDTO> execute(
            User loggedInUser,
            UUID municipalityId,
            UUID departmentId,
            AssignUsersToDepartmentRequestDTO request
    ) {
        AffiliatedUser affiliatedUser = this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        if (!affiliatedUser.isAdmin()) {
            throw new MustBeAdminException();
        }

        Department department = this.departmentRepository.findById(departmentId)
                .orElseThrow(DepartmentNotFoundException::new);

        List<AssignedUser> assignedUsers = new LinkedList<>();

        for (UUID id : request.ids()) {
            AffiliatedUser targetAffiliatedUser = this.authorizationService
                    .ensureUserIsAffiliatedTo(id, municipalityId);

            AssignedUser assignedUser = this.assignedUserRepository
                    .findByDepartmentIdAndAffiliatedUserId(departmentId, targetAffiliatedUser.getId())
                    .orElseGet(() -> affiliatedUser.assign(targetAffiliatedUser).to(department));

            assignedUsers.add(assignedUser);
        }

        return this.assignedUserRepository.saveAll(assignedUsers)
                .stream()
                .map(this.assignedUserMapper::toDto)
                .toList();
    }

}
