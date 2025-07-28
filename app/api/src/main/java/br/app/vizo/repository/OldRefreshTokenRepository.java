package br.app.vizo.repository;

import br.app.vizo.domain.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OldRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenAndUserId(String token, UUID userId);

    void deleteByTokenAndUserId(String token, UUID userId);
}
