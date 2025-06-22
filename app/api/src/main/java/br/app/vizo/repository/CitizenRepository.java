package br.app.vizo.repository;

import br.app.vizo.domain.user.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, UUID> {

    Optional<Citizen> findByDocument(String document);

    Optional<Citizen> findByDocumentOrEmail(String document, String email);
}