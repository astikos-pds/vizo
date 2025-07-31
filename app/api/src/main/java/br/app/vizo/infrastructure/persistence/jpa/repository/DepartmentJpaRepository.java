package br.app.vizo.infrastructure.persistence.jpa.repository;

import br.app.vizo.infrastructure.persistence.jpa.entity.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepartmentJpaRepository extends JpaRepository<DepartmentEntity, UUID> {

    Page<DepartmentEntity> findAllByMunicipalityId(UUID id, Pageable pageable);
}
