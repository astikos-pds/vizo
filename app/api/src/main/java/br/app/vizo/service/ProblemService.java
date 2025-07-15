package br.app.vizo.service;

import br.app.vizo.controller.request.UpdateProblemRequestDTO;
import br.app.vizo.controller.response.ProblemDTO;
import br.app.vizo.controller.response.ReportDTO;
import br.app.vizo.domain.problem.Problem;
import br.app.vizo.domain.user.User;
import br.app.vizo.exception.http.ForbiddenException;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.ProblemMapper;
import br.app.vizo.mapper.ReportMapper;
import br.app.vizo.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProblemService {

    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final ReportRepository reportRepository;
    private final ProblemMapper problemMapper;
    private final ReportMapper reportMapper;

    public ProblemService(
            UserRepository userRepository,
            ProblemRepository problemRepository,
            ReportRepository reportRepository,
            ProblemMapper problemMapper,
            ReportMapper reportMapper
    ) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.reportRepository = reportRepository;
        this.problemMapper = problemMapper;
        this.reportMapper = reportMapper;
    }

    public List<ProblemDTO> getProblems() {
        return this.problemRepository.findAll()
                .stream()
                .map(this.problemMapper::toDto)
                .toList();
    }

    public List<ProblemDTO> getValidatedProblems() {
        return this.problemRepository.findAllByValidated(true)
                .stream()
                .map(this.problemMapper::toDto)
                .toList();
    }

    public Page<ReportDTO> getProblemReports(UUID id, Pageable pageable) {

        if (!this.problemRepository.existsById(id)) {
            throw new NotFoundException("Problem not found.");
        }

        return this.reportRepository
                .findByProblemId(id, pageable)
                .map(this.reportMapper::toDto);
    }

    public ProblemDTO updateProblem(UUID id, UpdateProblemRequestDTO body, Authentication authentication) {
        User user = this.userRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        if (!user.isOfficial()) {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }

        Problem problem = this.problemRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Problem not found.")
        );

        problem.setStatus(body.status());

        return this.problemMapper.toDto(
                this.problemRepository.save(problem)
        );
    }
}
