package br.app.vizo.dto.profile;

import br.app.vizo.domain.user.UserType;

public record ProfileDTO(
        UserType userType,
        UserProfileDTO profile
) {
}
