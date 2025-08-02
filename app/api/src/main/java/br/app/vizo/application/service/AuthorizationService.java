package br.app.vizo.application.service;

import br.app.vizo.application.exception.AffiliationRequiredException;
import br.app.vizo.application.exception.MunicipalityNotFoundException;
import br.app.vizo.core.affiliation.AffiliatedUserRepository;
import br.app.vizo.core.affiliation.AffiliationStatus;
import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.municipality.MunicipalityRepository;
import br.app.vizo.core.user.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorizationService {

    private final AffiliatedUserRepository affiliatedUserRepository;
    private final MunicipalityRepository municipalityRepository;

    public AuthorizationService(AffiliatedUserRepository affiliatedUserRepository, MunicipalityRepository municipalityRepository) {
        this.affiliatedUserRepository = affiliatedUserRepository;
        this.municipalityRepository = municipalityRepository;
    }

    public void ensureUserIsAffiliatedTo(User user, UUID municipalityId) {
        boolean municipalityExists = this.municipalityRepository.existsById(municipalityId);
        if (!municipalityExists) {
            throw new MunicipalityNotFoundException();
        }

        boolean userHasAnApprovedAffiliationInMunicipality = this.affiliatedUserRepository
                .existsByUserIdAndMunicipalityIdAndStatus(user.getId(), municipalityId, AffiliationStatus.APPROVED);

        if (!userHasAnApprovedAffiliationInMunicipality) {
            throw new AffiliationRequiredException();
        }
    }
}
