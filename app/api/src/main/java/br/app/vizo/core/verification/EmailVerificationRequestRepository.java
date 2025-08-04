package br.app.vizo.core.verification;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRequestRepository {

    void save(EmailVerificationRequest emailVerificationRequest);

    Optional<EmailVerificationRequest> findById(UUID id);

    Optional<EmailVerificationRequest> findByEmailAndPurpose(String email, VerificationPurpose purpose);
}
