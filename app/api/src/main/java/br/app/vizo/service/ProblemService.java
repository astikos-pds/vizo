package br.app.vizo.service;

import br.app.vizo.controller.response.ProblemDTO;
import br.app.vizo.controller.response.ReportDTO;
import br.app.vizo.domain.problem.Problem;
import br.app.vizo.mapper.ProblemMapper;
import br.app.vizo.mapper.ReportMapper;
import br.app.vizo.repository.ProblemRepository;
import br.app.vizo.repository.ReportRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public List<ProblemDTO> getProblems() {
        return this.problemRepository.findAll()
                .stream()
                .map(this.problemMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ProblemDTO> getValidatedProblems() {
        return this.problemRepository.findByValidated(true)
                .stream()
                .map(this.problemMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ReportDTO> getReportsOfProblem(String id) {
        UUID problemId = UUID.fromString(id);

        if (!this.problemRepository.existsById(problemId)) {
            throw new RuntimeException("Problem not found");
        }

        return this.reportRepository.findByProblemId(problemId, Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this.reportMapper::toDto)
                .collect(Collectors.toList());
    }
}
