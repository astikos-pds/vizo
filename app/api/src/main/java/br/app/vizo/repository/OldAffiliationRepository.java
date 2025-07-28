package br.app.vizo.repository;

import br.app.vizo.domain.affiliation.Affiliation;
import br.app.vizo.domain.affiliation.AffiliationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OldAffiliationRepository extends JpaRepository<Affiliation, UUID> {

    Page<Affiliation> findAllByMunicipalityId(UUID municipalityId, Pageable pageable);

    Page<Affiliation> findAllByMunicipalityIdAndStatus(
            UUID municipalityId,
            AffiliationStatus status,
            Pageable pageable
    );

    boolean existsByMunicipalityIdAndUserIdAndStatus(
            UUID municipalityId,
            UUID userId,
            AffiliationStatus status
    );

    List<Affiliation> findAllByUserId(UUID userId);
}
