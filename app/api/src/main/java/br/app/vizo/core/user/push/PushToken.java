package br.app.vizo.core.user.push;

import br.app.vizo.core.user.UserId;

import java.time.Instant;
import java.util.UUID;

public class PushToken {

    private final Long id;
    private final UserId userId;
    private final String token;
    private final Platform platform;
    private final Instant createdAt;
    private Instant lastUsedAt;

    public PushToken(Long id, UserId userId, String token, Platform platform, Instant createdAt, Instant lastUsedAt) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.platform = platform;
        this.createdAt = createdAt;
        this.lastUsedAt = lastUsedAt;
    }

    public PushToken(UserId userId, String token, Platform platform) {
        this(0L, userId, token, platform, Instant.now(), Instant.now());
    }

    public void use() {
        this.lastUsedAt = Instant.now();
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

    public Platform getPlatform() {
        return platform;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getLastUsedAt() {
        return lastUsedAt;
    }
}
