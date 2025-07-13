package br.app.vizo.controller.response;

import br.app.vizo.domain.municipality.Municipality;

import java.time.Instant;
import java.util.UUID;

public record MunicipalityDTO(
        UUID id,
        String name,
        String emailDomain,
        String iconUrl,
        Instant createdAt,
        Instant updatedAt
) {
    public static MunicipalityDTO of(Municipality municipality) {
        return new MunicipalityDTO(
                municipality.getId(),
                municipality.getName(),
                municipality.getEmailDomain(),
                municipality.getIconUrl(),
                municipality.getCreatedAt(),
                municipality.getUpdatedAt()
        );
    }
}
