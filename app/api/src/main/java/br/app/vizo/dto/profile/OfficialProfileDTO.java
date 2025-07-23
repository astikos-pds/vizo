package br.app.vizo.dto.profile;

import br.app.vizo.domain.user.Official;
import br.app.vizo.domain.user.OfficialRole;

import java.util.UUID;

public record OfficialProfileDTO(
        UUID id,
        String name,
        String email,
        String document,
        OfficialRole role,
        Boolean wasApproved
) implements UserProfileDTO {
    public OfficialProfileDTO(Official official) {
        this(
                official.getId(),
                official.getName(),
                official.getEmail(),
                official.getDocument(),
                official.getRole(),
                official.getWasApproved()
        );
    }
}
