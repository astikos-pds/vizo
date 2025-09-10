package br.app.vizo.infrastructure.persistence.jpa.repository;

import br.app.vizo.infrastructure.persistence.jpa.entity.MunicipalityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MunicipalityJpaRepository extends JpaRepository<MunicipalityEntity, UUID> {

    @Query(value = "SELECT * FROM municipalities m WHERE :email_domain LIKE CONCAT('%', m.email_domain)", nativeQuery = true)
    Optional<MunicipalityEntity> findByEmailDomain(@Param("email_domain") String emailDomain);
}
