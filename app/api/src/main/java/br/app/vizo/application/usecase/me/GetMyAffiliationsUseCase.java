package br.app.vizo.application.usecase.me;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.AffiliatedUserDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.AffiliatedUserMapper;
import br.app.vizo.application.usecase.me.filter.GetMyAffiliationsParams;
import br.app.vizo.core.affiliation.AffiliatedUserRepository;
import br.app.vizo.core.user.User;

@UseCase
public class GetMyAffiliationsUseCase {

    private final AffiliatedUserRepository affiliatedUserRepository;
    private final AffiliatedUserMapper affiliatedUserMapper;

    public GetMyAffiliationsUseCase(AffiliatedUserRepository affiliatedUserRepository, AffiliatedUserMapper affiliatedUserMapper) {
        this.affiliatedUserRepository = affiliatedUserRepository;
        this.affiliatedUserMapper = affiliatedUserMapper;
    }

    public PageDTO<AffiliatedUserDTO> execute(User loggedInUser, PaginationDTO pagination, GetMyAffiliationsParams params) {
        if (params.status() != null) {
            return this.affiliatedUserRepository.findAllByUserIdAndStatus(loggedInUser.getId(), params.status(), pagination)
                    .map(this.affiliatedUserMapper::toDto);
        }

        return this.affiliatedUserRepository.findAllByUserId(loggedInUser.getId(), pagination)
                .map(this.affiliatedUserMapper::toDto);
    }
}
