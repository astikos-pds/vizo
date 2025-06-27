package br.app.vizo.service;

import br.app.vizo.controller.response.ProblemDTO;
import br.app.vizo.controller.response.ReportDTO;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.ProblemMapper;
import br.app.vizo.mapper.ReportMapper;
import br.app.vizo.repository.ProblemRepository;
import br.app.vizo.repository.ReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final ReportRepository reportRepository;
    private final ProblemMapper problemMapper;
    private final ReportMapper reportMapper;

    public ProblemService(
            ProblemRepository problemRepository,
            ReportRepository reportRepository,
            ProblemMapper problemMapper,
            ReportMapper reportMapper
    ) {
        this.problemRepository = problemRepository;
        this.reportRepository = reportRepository;
        this.problemMapper = problemMapper;
        this.reportMapper = reportMapper;
    }

    public Page<ProblemDTO> getProblems(Pageable pageable) {
        return this.problemRepository.findAll(pageable)
                .map(this.problemMapper::toDto);
    }

    public Page<ProblemDTO> getValidatedProblems(Pageable pageable) {
        return this.problemRepository.findByValidated(true, pageable)
                .map(this.problemMapper::toDto);
    }

    public Page<ReportDTO> getProblemReports(String id, Pageable pageable) {
        UUID problemId = UUID.fromString(id);

        if (!this.problemRepository.existsById(problemId)) {
            throw new NotFoundException("Problem not found.");
        }

        return this.reportRepository
                .findByProblemId(problemId, pageable)
                .map(this.reportMapper::toDto);
    }
}
