package br.app.vizo.service;

import br.app.vizo.controller.request.UpdateProblemRequestDTO;
import br.app.vizo.dto.ProblemDTO;
import br.app.vizo.dto.ReportDTO;
import br.app.vizo.domain.problem.Problem;
import br.app.vizo.domain.problem.ProblemType;
import br.app.vizo.domain.user.User;
import br.app.vizo.exception.NotFoundException;
import br.app.vizo.mapper.OldProblemMapper;
import br.app.vizo.mapper.OldReportMapper;
import br.app.vizo.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ProblemService {

    private final OldUserRepository oldUserRepository;
    private final OldProblemRepository oldProblemRepository;
    private final OldReportRepository oldReportRepository;
    private final OldProblemMapper oldProblemMapper;
    private final OldReportMapper oldReportMapper;

    public ProblemService(
            OldUserRepository oldUserRepository,
            OldProblemRepository oldProblemRepository,
            OldReportRepository oldReportRepository,
            OldProblemMapper oldProblemMapper,
            OldReportMapper oldReportMapper
    ) {
        this.oldUserRepository = oldUserRepository;
        this.oldProblemRepository = oldProblemRepository;
        this.oldReportRepository = oldReportRepository;
        this.oldProblemMapper = oldProblemMapper;
        this.oldReportMapper = oldReportMapper;
    }

    public List<ProblemDTO> getProblems() {
        return this.oldProblemRepository.findAll()
                .stream()
                .map(this.oldProblemMapper::toDto)
                .toList();
    }

    public List<ProblemDTO> getValidatedProblems() {
        return this.oldProblemRepository.findAllByValidated(true)
                .stream()
                .map(this.oldProblemMapper::toDto)
                .toList();
    }

    public Page<ReportDTO> getProblemReports(UUID id, Pageable pageable) {

        if (!this.oldProblemRepository.existsById(id)) {
            throw new NotFoundException("Problem not found.");
        }

        return this.oldReportRepository
                .findByProblemId(id, pageable)
                .map(this.oldReportMapper::toDto);
    }

    public List<ProblemType> getProblemTypes(Authentication authentication) {
        this.oldUserRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        return Arrays.stream(ProblemType.values()).sorted().toList();
    }

    public ProblemDTO updateProblem(UUID id, UpdateProblemRequestDTO body, Authentication authentication) {
        User user = this.oldUserRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        Problem problem = this.oldProblemRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Problem not found.")
        );

        problem.setStatus(body.status());

        return this.oldProblemMapper.toDto(
                this.oldProblemRepository.save(problem)
        );
    }
}
