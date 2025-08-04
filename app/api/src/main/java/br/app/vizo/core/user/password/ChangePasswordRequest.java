package br.app.vizo.core.user.password;

import br.app.vizo.core.shared.Code;
import br.app.vizo.core.shared.ExpirationTimestamp;
import br.app.vizo.core.user.User;
import br.app.vizo.core.user.exception.ChangePasswordRequestExpiredException;
import br.app.vizo.core.verification.exception.CodesDoNotMatchException;

import java.time.Instant;
import java.util.UUID;

public class ChangePasswordRequest {

    private final UUID id;
    private final User user;
    private Code code;
    private boolean verified;
    private ExpirationTimestamp expiresAt;
    private final Instant createdAt;

    public ChangePasswordRequest(UUID id, User user, Code code, boolean verified, ExpirationTimestamp expiresAt, Instant createdAt) {
        this.id = id;
        this.user = user;
        this.code = code;
        this.verified = verified;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public ChangePasswordRequest(User user, Code code, boolean verified, ExpirationTimestamp expiresAt) {
        this(
                UUID.randomUUID(),
                user,
                code,
                verified,
                expiresAt,
                Instant.now()
        );
    }

    public void retry() {
        this.code = Code.generate();
        this.expiresAt = ExpirationTimestamp.fromNowPlusMinutes(15);
    }

    public void verifyWith(String rawCode) {
        if (this.isExpired()) {
            throw new ChangePasswordRequestExpiredException();
        }

        if (!this.code.matches(rawCode)) {
            throw new CodesDoNotMatchException();
        }

        this.verified = true;
    }

    public boolean isExpired() {
        return this.expiresAt.isExpired();
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getCode() {
        return code.value();
    }

    public int getCodeLength() {
        return code.getLength();
    }

    public boolean isVerified() {
        return verified;
    }

    public Instant getExpiresAt() {
        return expiresAt.value();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
