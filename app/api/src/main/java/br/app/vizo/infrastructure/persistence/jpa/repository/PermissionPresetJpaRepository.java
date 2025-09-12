package br.app.vizo.infrastructure.persistence.jpa.repository;

import br.app.vizo.infrastructure.persistence.jpa.entity.PermissionPresetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionPresetJpaRepository extends JpaRepository<PermissionPresetEntity, UUID> {

    List<PermissionPresetEntity> findAllByMunicipalityId(UUID municipalityId);

    boolean existsByMunicipalityIdAndName(UUID municipalityId, String name);

}
