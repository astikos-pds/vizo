package br.app.vizo.repository;

import br.app.vizo.domain.verification.EmailVerificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailVerificationRequestRepository extends JpaRepository<EmailVerificationRequest, UUID> {
}
