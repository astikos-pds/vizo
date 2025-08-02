package br.app.vizo.infrastructure.persistence.jpa.repository;

import br.app.vizo.infrastructure.persistence.jpa.entity.AssignedUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssignedUserJpaRepository extends JpaRepository<AssignedUserEntity, UUID> {

    Optional<AssignedUserEntity> findByDepartmentIdAndAffiliatedUserId(UUID departmentId, UUID userId);

    Page<AssignedUserEntity> findAllByDepartmentId(UUID departmentId, Pageable pageable);

    Page<AssignedUserEntity> findAllByDepartmentMunicipalityIdAndAffiliatedUserId(
            UUID municipalityId,
            UUID userId,
            Pageable pageable
    );

    void deleteAllByDepartmentId(UUID departmentId);

}
