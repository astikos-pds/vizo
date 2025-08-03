package br.app.vizo.infrastructure.persistence.jpa.repository;

import br.app.vizo.core.affiliation.AffiliationStatus;
import br.app.vizo.infrastructure.persistence.jpa.entity.AffiliatedUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AffiliatedUserJpaRepository extends JpaRepository<AffiliatedUserEntity, UUID> {

    void deleteByUserIdAndMunicipalityId(UUID userId, UUID municipalityId);

    Page<AffiliatedUserEntity> findAllByMunicipalityId(UUID municipalityId, Pageable pageable);

    Page<AffiliatedUserEntity> findAllByMunicipalityIdAndStatus(
            UUID municipalityId,
            AffiliationStatus status,
            Pageable pageable
    );

    Optional<AffiliatedUserEntity> findByUserIdAndMunicipalityIdAndStatus(UUID municipalityId, UUID userId, AffiliationStatus status);

    Page<AffiliatedUserEntity> findAllByUserId(UUID userId, Pageable pageable);

    long countByMunicipalityIdAndIsAdmin(UUID municipalityId, boolean isAdmin);

    @Query(value = """
            SELECT a FROM AffiliatedUserEntity a
            WHERE a.municipality.id = :municipalityId
              AND a.status = 'APPROVED'
              AND a.isAdmin = false
            ORDER BY a.affiliatedAt ASC
    """)
    Optional<AffiliatedUserEntity> findFirstApprovedNonAdminByMunicipalityId(UUID municipalityId);
}
