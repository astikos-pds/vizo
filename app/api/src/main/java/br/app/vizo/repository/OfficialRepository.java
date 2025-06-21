package br.app.vizo.repository;

import br.app.vizo.domain.user.Official;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OfficialRepository extends JpaRepository<Official, UUID> {

    Optional<Official> findByDocumentOrEmail(String document, String email);
}
