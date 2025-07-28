package br.app.vizo.repository;

import br.app.vizo.domain.verification.EmailVerificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OldEmailVerificationRequestRepository extends JpaRepository<EmailVerificationRequest, UUID> {

    Optional<EmailVerificationRequest> findByEmail(String email);
}
