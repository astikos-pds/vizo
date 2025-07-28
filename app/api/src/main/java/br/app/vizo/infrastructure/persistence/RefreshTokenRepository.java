package br.app.vizo.infrastructure.persistence;

import br.app.vizo.core.user.token.RefreshToken;
import br.app.vizo.infrastructure.persistence.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshToken> findByTokenAndUserId(String token, UUID userId);

    void deleteByTokenAndUserId(String token, UUID userId);
}
