package br.app.vizo.core.verification;

import br.app.vizo.core.shared.Email;
import br.app.vizo.core.shared.ExpirationTimestamp;
import br.app.vizo.core.verification.exception.CodesDoNotMatchException;
import br.app.vizo.core.verification.exception.EmailVerificationRequestExpiredException;

import java.util.UUID;

public class EmailVerificationRequest {

    private final UUID id;
    private final Email email;
    private Code code;
    private boolean verified;
    private ExpirationTimestamp expiresAt;

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
    }

    public static EmailVerificationRequest create(String rawEmail) {
        Email email = new Email(rawEmail);
        Code code = Code.generate();

        ExpirationTimestamp expiresAt = ExpirationTimestamp.fromNowPlusMinutes(15);

        return new EmailVerificationRequest(email, code, false, expiresAt);
    }

    public boolean isExpired() {
        return this.expiresAt.isExpired();
    }

    public void retry() {
        this.code = Code.generate();
        this.expiresAt = ExpirationTimestamp.fromNowPlusMinutes(15);
    }

    public void verify(String rawCode) {
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

    public Email getEmail() {
        return email;
    }
}
