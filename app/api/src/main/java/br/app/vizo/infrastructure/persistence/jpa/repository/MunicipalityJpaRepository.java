package br.app.vizo.infrastructure.persistence.jpa.repository;

import br.app.vizo.infrastructure.persistence.jpa.entity.MunicipalityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MunicipalityJpaRepository extends JpaRepository<MunicipalityEntity, UUID> {

    Optional<MunicipalityEntity> findByEmailDomain(String emailDomain);
}
