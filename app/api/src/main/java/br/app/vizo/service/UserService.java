package br.app.vizo.service;

import br.app.vizo.dto.AffiliationDTO;
import br.app.vizo.dto.AssignmentDTO;
import br.app.vizo.dto.UserDTO;
import br.app.vizo.domain.user.User;
import br.app.vizo.exception.NotFoundException;
import br.app.vizo.mapper.AffiliationMapper;
import br.app.vizo.mapper.AssignmentMapper;
import br.app.vizo.mapper.UserMapper;
import br.app.vizo.repository.AffiliationRepository;
import br.app.vizo.repository.AssignmentRepository;
import br.app.vizo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AffiliationRepository affiliationRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserMapper userMapper;
    private final AffiliationMapper affiliationMapper;
    private final AssignmentMapper assignmentMapper;

    public UserService(
            UserRepository userRepository,
            AffiliationRepository affiliationRepository,
            AssignmentRepository assignmentRepository,
            UserMapper userMapper,
            AffiliationMapper affiliationMapper,
            AssignmentMapper assignmentMapper
    ) {
        this.userRepository = userRepository;
        this.affiliationRepository = affiliationRepository;
        this.assignmentRepository = assignmentRepository;
        this.userMapper = userMapper;
        this.affiliationMapper = affiliationMapper;
        this.assignmentMapper = assignmentMapper;
    }

    public UserDTO getLoggedInUser(Authentication authentication) {
        User user = this.userRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        return this.userMapper.toDto(user);
    }

    public List<AffiliationDTO> getAffiliations(Authentication authentication) {
        User user = this.userRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        return this.affiliationRepository
                .findAllByUserId(user.getId())
                .stream()
                .map(this.affiliationMapper::toDto)
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
