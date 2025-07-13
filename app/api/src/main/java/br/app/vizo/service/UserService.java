package br.app.vizo.service;

import br.app.vizo.controller.response.profile.ProfileDTO;
import br.app.vizo.controller.response.profile.UserProfileDTO;
import br.app.vizo.domain.user.User;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProfileDTO getLoggedInUser(Authentication authentication) {
        User user = this.userRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        return new ProfileDTO(user.getType(), UserProfileDTO.of(user));
    }
}
