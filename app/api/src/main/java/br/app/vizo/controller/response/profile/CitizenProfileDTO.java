package br.app.vizo.controller.response.profile;

import br.app.vizo.domain.user.Citizen;

import java.util.UUID;

public record CitizenProfileDTO(
        UUID id,
        String name,
        String email,
        String document,
        Double credibilityPoints
) implements UserProfileDTO {
    public CitizenProfileDTO(Citizen citizen) {
        this(
                citizen.getId(),
                citizen.getName(),
                citizen.getEmail(),
                citizen.getDocument(),
                citizen.getCredibilityPoints()
        );
    }
}