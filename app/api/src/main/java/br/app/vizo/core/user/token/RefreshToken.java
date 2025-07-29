package br.app.vizo.core.user.token;

import br.app.vizo.core.shared.ExpirationTimestamp;
import br.app.vizo.core.user.UserId;

import java.time.Instant;
import java.util.UUID;

public class RefreshToken {

    private final Long id;
    private final UserId userId;
    private final String token;
    private final ExpirationTimestamp expiresAt;
    private final Instant createdAt;

    public RefreshToken(UserId userId, String token, ExpirationTimestamp expiresAt) {
        this.id = 1L;
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.createdAt = Instant.now();
    }

    public boolean isExpired() {
        return this.expiresAt.isExpired();
    }

    public Long getId() {
        return id;
    }

    public UUID getUserId() {
        return userId.value();
    }

    public String getToken() {
        return token;
    }

    public Instant getExpiresAt() {
        return expiresAt.value();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
