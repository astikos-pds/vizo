package br.app.vizo.infrastructure.persistence;

import br.app.vizo.infrastructure.persistence.entity.EmailVerificationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRequestRepository extends JpaRepository<EmailVerificationRequestEntity, UUID> {

    Optional<EmailVerificationRequestEntity> findByEmail(String email);
}
