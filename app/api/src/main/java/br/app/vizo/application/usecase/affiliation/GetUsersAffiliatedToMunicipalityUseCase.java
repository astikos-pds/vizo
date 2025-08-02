package br.app.vizo.application.usecase.affiliation;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.AffiliatedUserDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.AffiliatedUserMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.application.usecase.affiliation.filter.AffiliationFilter;
import br.app.vizo.core.affiliation.AffiliatedUserRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetUsersAffiliatedToMunicipalityUseCase {

    private final AuthorizationService authorizationService;
    private final AffiliatedUserRepository affiliatedUserRepository;
    private final AffiliatedUserMapper affiliatedUserMapper;

    public PageDTO<AffiliatedUserDTO> execute(User loggedInUser, UUID municipalityId, PaginationDTO pagination, AffiliationFilter filter) {
        this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        if (filter.status() == null) {
            return this.affiliatedUserRepository.findAllByMunicipalityId(municipalityId, pagination)
                    .map(this.affiliatedUserMapper::toDto);
        }

        return this.affiliatedUserRepository.findAllByMunicipalityIdAndStatus(municipalityId, filter.status(), pagination)
                .map(this.affiliatedUserMapper::toDto);
    }
}
