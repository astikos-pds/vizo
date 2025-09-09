package br.app.vizo.application.service;

import br.app.vizo.application.exception.AffiliationRequiredException;
import br.app.vizo.application.exception.AssignmentRequiredException;
import br.app.vizo.application.exception.DepartmentNotFoundException;
import br.app.vizo.application.exception.MunicipalityNotFoundException;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.affiliation.AffiliatedUserRepository;
import br.app.vizo.core.affiliation.AffiliationStatus;
import br.app.vizo.core.assignment.AssignedUser;
import br.app.vizo.core.assignment.AssignedUserRepository;
import br.app.vizo.core.department.DepartmentRepository;
import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.municipality.MunicipalityRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AffiliatedUserRepository affiliatedUserRepository;
    private final MunicipalityRepository municipalityRepository;
    private final AssignedUserRepository assignedUserRepository;
    private final DepartmentRepository departmentRepository;

    public AffiliatedUser ensureUserIsAffiliatedTo(UUID userId, UUID municipalityId) {
        boolean municipalityExists = this.municipalityRepository.existsById(municipalityId);
        if (!municipalityExists) {
            throw new MunicipalityNotFoundException();
        }

        return this.affiliatedUserRepository
                .findByUserIdAndMunicipalityIdAndStatus(userId, municipalityId, AffiliationStatus.APPROVED)
                .orElseThrow(AffiliationRequiredException::new);
    }

    public AffiliatedUser ensureUserIsAffiliatedTo(User user, UUID municipalityId) {
        return this.ensureUserIsAffiliatedTo(user.getId(), municipalityId);
    }

    public AssignedUser ensureUserIsAssignedTo(User user, UUID municipalityId, UUID departmentId) {
        AffiliatedUser affiliatedUser = this.ensureUserIsAffiliatedTo(user, municipalityId);

        boolean departmentExists = this.departmentRepository.existsById(departmentId);
        if (!departmentExists) {
            throw new DepartmentNotFoundException();
        }

        return this.assignedUserRepository
                .findByDepartmentIdAndAffiliatedUserId(departmentId, affiliatedUser.getId())
                .orElseThrow(AssignmentRequiredException::new);
    }
}
