package br.app.vizo.dto;

import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.user.User;

public record AffiliatedUserContextDTO(
        Municipality municipality,
        User loggedInUser
) {}