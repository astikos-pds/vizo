package br.app.vizo.repository;

import br.app.vizo.domain.affiliation.AffiliationRequest;
import br.app.vizo.domain.affiliation.AffiliationRequestStatus;
import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.user.Official;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AffiliationRequestRepository extends JpaRepository<AffiliationRequest, UUID> {

    Page<AffiliationRequest> findAllByMunicipalityId(UUID municipalityId, Pageable pageable);

    Page<AffiliationRequest> findAllByMunicipalityIdAndStatus(
            UUID municipalityId,
            AffiliationRequestStatus status,
            Pageable pageable
    );

    boolean existsByMunicipalityIdAndOfficialIdAndStatus(
            UUID municipalityId,
            UUID officialId,
            AffiliationRequestStatus status
    );

    @Query("""
        SELECT ar.municipality
        FROM AffiliationRequest ar
        WHERE ar.official = :official AND ar.status = 'APPROVED'
    """)
    List<Municipality> findAllAffiliatedMunicipalitiesByOfficial(@Param("official") Official official);
}
