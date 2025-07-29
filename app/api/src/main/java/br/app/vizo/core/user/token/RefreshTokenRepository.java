package br.app.vizo.core.user.token;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {

    void save(RefreshToken refreshToken);

    boolean existsByTokenAndUserId(String token, UUID userId);

    void deleteByTokenAndUserId(String token, UUID userId);
}
