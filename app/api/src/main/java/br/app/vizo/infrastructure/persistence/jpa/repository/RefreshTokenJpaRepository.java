package br.app.vizo.infrastructure.persistence.jpa.repository;

import br.app.vizo.infrastructure.persistence.jpa.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByTokenAndUserId(String token, UUID userId);

    void deleteByTokenAndUserId(String token, UUID userId);
}
