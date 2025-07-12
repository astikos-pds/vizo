package br.app.vizo.domain.verification;

import br.app.vizo.util.DateUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "email_verification_requests")
@Getter
@Setter
@AllArgsConstructor
public class EmailVerificationRequest {

    @Id
    private UUID id;

    private String email;

    private String code;

    private Boolean verified;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "created_at")
    private Instant createdAt;

    public boolean isVerified() {
        return this.verified == true;
    }

    public boolean isExpired() {
        return DateUtil.now().isAfter(this.expiresAt);
    }

    public EmailVerificationRequest() {
        this(UUID.randomUUID(), "", "", false, DateUtil.now(), DateUtil.now());
    }
}
