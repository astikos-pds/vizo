package br.app.vizo.infrastructure.persistence.jpa.entity;

import br.app.vizo.core.verification.VerificationPurpose;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "email_verification_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationRequestEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Boolean verified;

    @Enumerated(value = EnumType.STRING)
    private VerificationPurpose purpose;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}
