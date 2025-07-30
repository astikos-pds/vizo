package br.app.vizo.core.verification;

import br.app.vizo.core.shared.Email;
import br.app.vizo.core.shared.ExpirationTimestamp;
import br.app.vizo.core.verification.exception.CodesDoNotMatchException;
import br.app.vizo.core.verification.exception.EmailVerificationRequestExpiredException;

import java.time.Instant;
import java.util.UUID;

public class EmailVerificationRequest {

    private final UUID id;
    private final Email email;
    private Code code;
    private boolean verified;
    private ExpirationTimestamp expiresAt;
    private final Instant createdAt;

    public EmailVerificationRequest(UUID id, Email email, Code code, boolean verified, ExpirationTimestamp expiresAt, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.code = code;
        this.verified = verified;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public EmailVerificationRequest(
            Email email,
            Code code,
            boolean verified,
            ExpirationTimestamp expiresAt
    ) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.code = code;
        this.verified = verified;
        this.expiresAt = expiresAt;
        this.createdAt = Instant.now();
    }

    public boolean isExpired() {
        return this.expiresAt.isExpired();
    }

    public boolean isVerified() {
        return verified;
    }

    public void retry() {
        this.code = Code.generate();
        this.expiresAt = ExpirationTimestamp.fromNowPlusMinutes(15);
    }

    public void verifyWith(String rawCode) {
        if (this.isExpired()) {
            throw new EmailVerificationRequestExpiredException();
        }

        if (!this.code.matches(rawCode)) {
            throw new CodesDoNotMatchException();
        }

        this.verified = true;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email.value();
    }

    public String getCode() {
        return code.value();
    }

    public int getCodeLength() {
        return code.getLength();
    }

    public Instant getExpiresAt() {
        return expiresAt.value();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
