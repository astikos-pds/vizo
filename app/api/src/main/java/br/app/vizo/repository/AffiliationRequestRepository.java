package br.app.vizo.repository;

import br.app.vizo.domain.affiliation.AffiliationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AffiliationRequestRepository extends JpaRepository<AffiliationRequest, UUID> {
}
