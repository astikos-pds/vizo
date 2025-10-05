package br.app.vizo.infrastructure.persistence.jpa.repository;

import br.app.vizo.infrastructure.persistence.jpa.entity.PushTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PushTokenJpaRepository extends JpaRepository<PushTokenEntity, Long> {

    Optional<PushTokenEntity> findByToken(String token);

    List<PushTokenEntity> findAllByUserId(UUID userId);
}
