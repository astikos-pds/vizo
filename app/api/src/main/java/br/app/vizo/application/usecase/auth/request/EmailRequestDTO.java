package br.app.vizo.application.usecase.auth.request;

import br.app.vizo.core.verification.VerificationPurpose;

public record EmailRequestDTO(
        String email,
        VerificationPurpose purpose
) {
}
