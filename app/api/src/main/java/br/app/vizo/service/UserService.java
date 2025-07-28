package br.app.vizo.service;

import br.app.vizo.dto.AffiliationDTO;
import br.app.vizo.dto.AssignmentDTO;
import br.app.vizo.dto.UserDTO;
import br.app.vizo.domain.user.User;
import br.app.vizo.exception.NotFoundException;
import br.app.vizo.mapper.OldAffiliationMapper;
import br.app.vizo.mapper.OldAssignmentMapper;
import br.app.vizo.mapper.OldUserMapper;
import br.app.vizo.repository.OldAffiliationRepository;
import br.app.vizo.repository.OldAssignmentRepository;
import br.app.vizo.repository.OldUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final OldUserRepository oldUserRepository;
    private final OldAffiliationRepository oldAffiliationRepository;
    private final OldAssignmentRepository oldAssignmentRepository;
    private final OldUserMapper oldUserMapper;
    private final OldAffiliationMapper oldAffiliationMapper;
    private final OldAssignmentMapper oldAssignmentMapper;

    public UserService(
            OldUserRepository oldUserRepository,
            OldAffiliationRepository oldAffiliationRepository,
            OldAssignmentRepository oldAssignmentRepository,
            OldUserMapper oldUserMapper,
            OldAffiliationMapper oldAffiliationMapper,
            OldAssignmentMapper oldAssignmentMapper
    ) {
        this.oldUserRepository = oldUserRepository;
        this.oldAffiliationRepository = oldAffiliationRepository;
        this.oldAssignmentRepository = oldAssignmentRepository;
        this.oldUserMapper = oldUserMapper;
        this.oldAffiliationMapper = oldAffiliationMapper;
        this.oldAssignmentMapper = oldAssignmentMapper;
    }

    public UserDTO getLoggedInUser(Authentication authentication) {
        User user = this.oldUserRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        return this.oldUserMapper.toDto(user);
    }

    public List<AffiliationDTO> getAffiliations(Authentication authentication) {
        User user = this.oldUserRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        return this.oldAffiliationRepository
                .findAllByUserId(user.getId())
                .stream()
                .map(this.oldAffiliationMapper::toDto)
                .toList();
    }

    public List<AssignmentDTO> getAssignments(UUID id, Authentication authentication) {
        User user = this.oldUserRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        return this.oldAssignmentRepository
                .findAllByDepartmentMunicipalityIdAndUserId(id, user.getId())
                .stream()
                .map(this.oldAssignmentMapper::toDto)
                .toList();
    }
}
