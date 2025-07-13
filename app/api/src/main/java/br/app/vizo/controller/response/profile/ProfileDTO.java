package br.app.vizo.controller.response.profile;

import br.app.vizo.domain.user.UserType;

public record ProfileDTO(
        UserType userType,
        UserProfileDTO profile
) {
}
