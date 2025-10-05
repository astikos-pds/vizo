package br.app.vizo.core.user.push;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PushTokenRepository {

    Optional<PushToken> findByToken(String token);

    List<PushToken> findAllByUserId(UUID id);
}
