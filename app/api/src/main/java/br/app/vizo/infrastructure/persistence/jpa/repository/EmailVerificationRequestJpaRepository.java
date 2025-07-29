package br.app.vizo.infrastructure.persistence.jpa.repository;

import br.app.vizo.infrastructure.persistence.jpa.entity.EmailVerificationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationRequestJpaRepository extends JpaRepository<EmailVerificationRequestEntity, UUID> {

    Optional<EmailVerificationRequestEntity> findByEmail(String email);
}
