package br.app.vizo.core.user.token;

import br.app.vizo.core.shared.ExpirationTimestamp;
import br.app.vizo.core.user.User;
import br.app.vizo.infrastructure.persistence.RefreshTokenRepository;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.sql.Ref;
import java.time.Instant;
import java.util.UUID;

public class RefreshToken {

    private final Long id;
    private final User user;
    private final String token;
    private final ExpirationTimestamp expiresAt;
    private final Instant createdAt;

    public RefreshToken(User user, String token, ExpirationTimestamp expiresAt) {
        this.id = 1L;
        this.user = user;
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
        return user.getId();
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
