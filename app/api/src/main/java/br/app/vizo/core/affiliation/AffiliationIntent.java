package br.app.vizo.core.affiliation;

import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.shared.Email;
import br.app.vizo.core.user.User;
import br.app.vizo.core.affiliation.exception.InvalidAffiliationException;

public record AffiliationIntent(
        User user,
        Municipality municipality
) {
    public AffiliatedUser with(String rawInstitutionalEmail) {
        Email institutionalEmail = new Email(rawInstitutionalEmail);

        if (!municipality.acceptsEmail(institutionalEmail.value())) {
            throw new InvalidAffiliationException();
        }

        return new AffiliatedUser(user, municipality, institutionalEmail);
    }
}
