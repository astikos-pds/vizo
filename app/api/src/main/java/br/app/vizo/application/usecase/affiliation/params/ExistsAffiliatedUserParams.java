package br.app.vizo.application.usecase.affiliation.params;

import br.app.vizo.core.shared.Email;

public record ExistsAffiliatedUserParams(Email institutionalEmail) {

    public ExistsAffiliatedUserParams(String email) {
        this(new Email(email));
    }

    public String getInstitutionalEmail() {
        return institutionalEmail.value();
    }
}
