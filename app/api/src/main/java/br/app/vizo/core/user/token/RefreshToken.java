package br.app.vizo.core.user.token;

import br.app.vizo.core.shared.ExpirationTimestamp;
import br.app.vizo.core.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshToken {

    @Getter private final UUID id;
    @Getter private final User user;
    @Getter private final String token;
    private final ExpirationTimestamp expiresAt;
    @Getter private final Instant createdAt;

    public RefreshToken(User user, String token, ExpirationTimestamp expiresAt) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.token = token;
        this.expiresAt = expiresAt;
        this.createdAt = Instant.now();
    }

    public boolean isExpired() {
        return this.expiresAt.isExpired();
    }
}
