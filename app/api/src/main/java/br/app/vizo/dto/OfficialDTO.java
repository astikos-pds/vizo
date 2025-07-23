package br.app.vizo.dto;

import br.app.vizo.domain.user.OfficialRole;

import java.time.Instant;
import java.util.UUID;

public record OfficialDTO(
        UUID id,
        String document,
        String email,
        String name,
        AvatarDTO avatar,
        OfficialRole role,
        Boolean wasApproved,
        Instant createdAt,
        Instant updatedAt
) {
}
