package br.app.vizo.application.usecase.department;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.exception.MustBeAdminException;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.assignment.AssignedUserRepository;
import br.app.vizo.core.department.DepartmentRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class DeleteDepartmentUseCase {

    private final AuthorizationService authorizationService;
    private final DepartmentRepository departmentRepository;
    private final AssignedUserRepository assignedUserRepository;

    @Transactional
    public void execute(User loggedInUser, UUID municipalityId, UUID departmentId) {
        AffiliatedUser affiliatedUser = this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        if (!affiliatedUser.isAdmin()) {
            throw new MustBeAdminException();
        }

        this.assignedUserRepository.deleteAllByDepartmentId(departmentId);

        this.departmentRepository.deleteById(departmentId);
    }
}
