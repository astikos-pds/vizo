package br.app.vizo.core.municipality;

import br.app.vizo.core.municipality.exception.InvalidEmailDomainException;
import br.app.vizo.core.shared.Email;

public record EmailDomain(
        String value
) {

    public EmailDomain {
        if (!value.matches("^[a-zA-Z0-9.-]+$")) {
            throw new InvalidEmailDomainException();
        }
    }

    public boolean matches(Email email) {
        return email.value().endsWith(value);
    }
}
