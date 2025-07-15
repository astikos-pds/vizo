package br.app.vizo.dto;

import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.user.Official;

public record OfficialContextDTO(
        Municipality municipality,
        Official loggedInOfficial
) {}