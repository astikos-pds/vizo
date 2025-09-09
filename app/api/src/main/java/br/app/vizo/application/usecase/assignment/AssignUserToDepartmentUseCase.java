package br.app.vizo.application.usecase.assignment;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.AssignedUserDTO;
import br.app.vizo.application.exception.AffiliationNotFoundException;
import br.app.vizo.application.exception.DepartmentNotFoundException;
import br.app.vizo.application.exception.MustBeAdminException;
import br.app.vizo.application.mapper.AssignedUserMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.application.usecase.assignment.request.AssignUserToDepartmentRequestDTO;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.affiliation.AffiliatedUserRepository;
import br.app.vizo.core.assignment.AssignedUser;
import br.app.vizo.core.assignment.AssignedUserRepository;
import br.app.vizo.core.department.Department;
import br.app.vizo.core.department.DepartmentRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class AssignUserToDepartmentUseCase {

    private final AuthorizationService authorizationService;
    private final DepartmentRepository departmentRepository;
    private final AssignedUserRepository assignedUserRepository;
    private final AssignedUserMapper assignedUserMapper;
    private final AffiliatedUserRepository affiliatedUserRepository;

    public AssignedUserDTO execute(User loggedInUser, UUID municipalityId, UUID departmentId, AssignUserToDepartmentRequestDTO request) {
        AffiliatedUser affiliatedUser = this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        if (!affiliatedUser.isAdmin()) {
            throw new MustBeAdminException();
        }

        Department department = this.departmentRepository.findById(departmentId)
                .orElseThrow(DepartmentNotFoundException::new);

        AffiliatedUser targetAffiliatedUser = this.affiliatedUserRepository.findById(request.affiliationId())
                .orElseThrow(AffiliationNotFoundException::new);

        AssignedUser assignedUser = this.assignedUserRepository
                .findByDepartmentIdAndAffiliatedUserId(departmentId, targetAffiliatedUser.getId())
                .orElseGet(() -> affiliatedUser.assign(targetAffiliatedUser).to(department));

        AssignedUser saved = this.assignedUserRepository.save(assignedUser);
        return this.assignedUserMapper.toDto(saved);
    }

}
