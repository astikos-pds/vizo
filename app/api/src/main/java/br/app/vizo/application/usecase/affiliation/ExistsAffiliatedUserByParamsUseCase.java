package br.app.vizo.application.usecase.affiliation;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.usecase.affiliation.params.ExistsAffiliatedUserParams;
import br.app.vizo.core.affiliation.AffiliatedUserRepository;

@UseCase
public class ExistsAffiliatedUserByParamsUseCase {

    private final AffiliatedUserRepository affiliatedUserRepository;

    public ExistsAffiliatedUserByParamsUseCase(AffiliatedUserRepository affiliatedUserRepository) {
        this.affiliatedUserRepository = affiliatedUserRepository;
    }

    public boolean execute(ExistsAffiliatedUserParams params) {
        return this.affiliatedUserRepository.existsByInstitutionalEmail(params.getInstitutionalEmail());
    }
}
