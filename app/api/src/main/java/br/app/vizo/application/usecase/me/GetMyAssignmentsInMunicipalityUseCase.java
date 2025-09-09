package br.app.vizo.application.usecase.me;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.AssignedUserDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.AssignedUserMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.assignment.AssignedUserRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetMyAssignmentsInMunicipalityUseCase {

    private final AuthorizationService authorizationService;
    private final AssignedUserRepository assignedUserRepository;
    private final AssignedUserMapper assignedUserMapper;

    public PageDTO<AssignedUserDTO> execute(User loggedInUser, UUID municipalityId, PaginationDTO pagination) {
        AffiliatedUser affiliatedUser = this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        return this.assignedUserRepository.findAllByAffiliatedUserId(affiliatedUser.getId(), pagination)
                .map(this.assignedUserMapper::toDto);
    }
}
