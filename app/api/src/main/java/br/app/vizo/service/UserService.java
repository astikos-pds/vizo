package br.app.vizo.service;

import br.app.vizo.dto.AffiliationRequestDTO;
import br.app.vizo.dto.AssignmentDTO;
import br.app.vizo.dto.UserDTO;
import br.app.vizo.domain.user.User;
import br.app.vizo.exception.NotFoundException;
import br.app.vizo.mapper.AffiliationRequestMapper;
import br.app.vizo.mapper.AssignmentMapper;
import br.app.vizo.mapper.UserMapper;
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
    private final UserMapper userMapper;
    private final AffiliationRequestMapper affiliationRequestMapper;
    private final AssignmentMapper assignmentMapper;

    public UserService(
            UserRepository userRepository,
            AffiliationRequestRepository affiliationRequestRepository,
            AssignmentRepository assignmentRepository,
            UserMapper userMapper,
            AffiliationRequestMapper affiliationRequestMapper,
            AssignmentMapper assignmentMapper
    ) {
        this.userRepository = userRepository;
        this.affiliationRequestRepository = affiliationRequestRepository;
        this.assignmentRepository = assignmentRepository;
        this.userMapper = userMapper;
        this.affiliationRequestMapper = affiliationRequestMapper;
        this.assignmentMapper = assignmentMapper;
    }

    public UserDTO getLoggedInUser(Authentication authentication) {
        User user = this.userRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        return this.userMapper.toDto(user);
    }

    public List<AffiliationRequestDTO> getAffiliations(Authentication authentication) {
        User user = this.userRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        return this.affiliationRequestRepository
                .findAllByUserId(user.getId())
                .stream()
                .map(this.affiliationRequestMapper::toDto)
                .toList();
    }

    public List<AssignmentDTO> getAssignments(UUID id, Authentication authentication) {
        User user = this.userRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        return this.assignmentRepository
                .findAllByDepartmentMunicipalityIdAndUserId(id, user.getId())
                .stream()
                .map(this.assignmentMapper::toDto)
                .toList();
    }
}
