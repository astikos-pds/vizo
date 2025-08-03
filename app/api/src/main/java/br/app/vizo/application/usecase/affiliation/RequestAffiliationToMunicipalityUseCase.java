package br.app.vizo.application.usecase.affiliation;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.AffiliatedUserDTO;
import br.app.vizo.application.exception.MunicipalityNotFoundException;
import br.app.vizo.application.mapper.AffiliatedUserMapper;
import br.app.vizo.application.usecase.affiliation.request.AffiliateToMunicipalityRequestDTO;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.affiliation.AffiliatedUserRepository;
import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.municipality.MunicipalityRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class RequestAffiliationToMunicipalityUseCase {

    private final AffiliatedUserRepository affiliatedUserRepository;
    private final AffiliatedUserMapper affiliatedUserMapper;
    private final MunicipalityRepository municipalityRepository;

    public AffiliatedUserDTO execute(User loggedInUser, UUID municipalityId, AffiliateToMunicipalityRequestDTO request) {
        String institutionalEmail = request.institutionalEmail();

        Municipality municipality = this.municipalityRepository.findById(municipalityId)
                .orElseThrow(MunicipalityNotFoundException::new);

        AffiliatedUser affiliatedUser = loggedInUser.requestAffiliationTo(municipality).with(institutionalEmail);

        AffiliatedUser saved = this.affiliatedUserRepository.save(affiliatedUser);
        return this.affiliatedUserMapper.toDto(saved);
    }
}
