package br.app.vizo.controller.response.profile;

import br.app.vizo.domain.user.Citizen;
import br.app.vizo.domain.user.Official;
import br.app.vizo.domain.user.User;

public interface UserProfileDTO {
    static UserProfileDTO of(User user) {
        if (user instanceof Citizen citizen) {
            return new CitizenProfileDTO(citizen);
        } else if (user instanceof Official official) {
            return new OfficialProfileDTO(official);
        } else {
            throw new IllegalArgumentException("User type not supported: " + user.getClass().getSimpleName());
        }
    }
}
