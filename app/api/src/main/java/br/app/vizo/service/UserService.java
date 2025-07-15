package br.app.vizo.service;

import br.app.vizo.controller.response.AffiliationRequestDTO;
import br.app.vizo.controller.response.profile.ProfileDTO;
import br.app.vizo.controller.response.profile.UserProfileDTO;
import br.app.vizo.domain.user.Official;
import br.app.vizo.domain.user.User;
import br.app.vizo.domain.user.UserType;
import br.app.vizo.exception.http.ForbiddenException;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.AffiliationRequestMapper;
import br.app.vizo.repository.AffiliationRequestRepository;
import br.app.vizo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AffiliationRequestRepository affiliationRequestRepository;
    private final AffiliationRequestMapper affiliationRequestMapper;

    public UserService(
            UserRepository userRepository,
            AffiliationRequestRepository affiliationRequestRepository,
            AffiliationRequestMapper affiliationRequestMapper
    ) {
        this.userRepository = userRepository;
        this.affiliationRequestRepository = affiliationRequestRepository;
        this.affiliationRequestMapper = affiliationRequestMapper;
    }

    public ProfileDTO getLoggedInUser(Authentication authentication) {
        User user = this.userRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        UserType userType;
        if (user.isOfficial()) {
            userType = UserType.OFFICIAL;
        } else {
            userType = UserType.CITIZEN;
        }

        return new ProfileDTO(userType, UserProfileDTO.of(user));
    }

    public List<AffiliationRequestDTO> getAffiliations(Authentication authentication) {
        User user = this.userRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        if (!user.isOfficial()) {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }

        Official official = (Official) user;

        return this.affiliationRequestRepository
                .findAllByOfficial(official)
                .stream()
                .map(this.affiliationRequestMapper::toDto)
                .toList();
    }
}
