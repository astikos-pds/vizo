package br.app.vizo.service;

import br.app.vizo.dto.AffiliationRequestDTO;
import br.app.vizo.dto.AssignmentDTO;
import br.app.vizo.dto.profile.ProfileDTO;
import br.app.vizo.dto.profile.UserProfileDTO;
import br.app.vizo.domain.user.Official;
import br.app.vizo.domain.user.User;
import br.app.vizo.domain.user.UserType;
import br.app.vizo.exception.ForbiddenException;
import br.app.vizo.exception.NotFoundException;
import br.app.vizo.mapper.AffiliationRequestMapper;
import br.app.vizo.mapper.AssignmentMapper;
import br.app.vizo.repository.AffiliationRequestRepository;
import br.app.vizo.repository.AssignmentRepository;
import br.app.vizo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AffiliationRequestRepository affiliationRequestRepository;
    private final AssignmentRepository assignmentRepository;
    private final AffiliationRequestMapper affiliationRequestMapper;
    private final AssignmentMapper assignmentMapper;

    public UserService(
            UserRepository userRepository,
            AffiliationRequestRepository affiliationRequestRepository,
            AssignmentRepository assignmentRepository,
            AffiliationRequestMapper affiliationRequestMapper,
            AssignmentMapper assignmentMapper
    ) {
        this.userRepository = userRepository;
        this.affiliationRequestRepository = affiliationRequestRepository;
        this.assignmentRepository = assignmentRepository;
        this.affiliationRequestMapper = affiliationRequestMapper;
        this.assignmentMapper = assignmentMapper;
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

    public List<AssignmentDTO> getAssignments(UUID id, Authentication authentication) {
        User user = this.userRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        if (!user.isOfficial()) {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }

        return this.assignmentRepository
                .findAllByDepartmentMunicipalityIdAndOfficialId(id, user.getId())
                .stream()
                .map(this.assignmentMapper::toDto)
                .toList();
    }
}
