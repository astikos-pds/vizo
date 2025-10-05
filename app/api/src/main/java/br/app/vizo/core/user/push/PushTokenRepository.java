package br.app.vizo.core.user.push;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PushTokenRepository {

    PushToken save(PushToken pushToken);

    Optional<PushToken> findByToken(String token);

    List<PushToken> findAllByUserIdIn(Set<UUID> usersIds);
}
