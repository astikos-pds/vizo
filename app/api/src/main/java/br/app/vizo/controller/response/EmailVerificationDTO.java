package br.app.vizo.controller.response;

import br.app.vizo.domain.verification.EmailVerificationRequest;

import java.time.Instant;
import java.util.UUID;

public record EmailVerificationDTO(
        UUID id,
        String email,
        Integer codeLength,
        Instant expiresAt,
        Instant createdAt
) {

    public static EmailVerificationDTO of(EmailVerificationRequest request) {
        return new EmailVerificationDTO(
                request.getId(),
                request.getEmail(),
                request.getCode().length(),
                request.getExpiresAt(),
                request.getCreatedAt()
        );
    }
}
