package br.app.vizo.application.dto;

import java.time.Instant;
import java.util.UUID;

public record EmailVerificationDTO(
        UUID id,
        Integer codeLength,
        Instant expiresAt,
        Instant createdAt
) {
}
