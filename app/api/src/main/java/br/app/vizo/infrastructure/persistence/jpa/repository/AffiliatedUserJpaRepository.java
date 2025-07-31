package br.app.vizo.infrastructure.persistence.jpa.repository;

import br.app.vizo.core.affiliation.AffiliationStatus;
import br.app.vizo.infrastructure.persistence.jpa.entity.AffiliatedUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AffiliatedUserJpaRepository extends JpaRepository<AffiliatedUserEntity, UUID> {

    Page<AffiliatedUserEntity> findAllByMunicipalityId(UUID municipalityId, Pageable pageable);

    Page<AffiliatedUserEntity> findAllByMunicipalityIdAndStatus(
            UUID municipalityId,
            AffiliationStatus status,
            Pageable pageable
    );

    boolean existsByUserIdAndMunicipalityIdAndStatus(UUID municipalityId, UUID userId, AffiliationStatus status);

    List<AffiliatedUserEntity> findAllByUserId(UUID userId);
}
