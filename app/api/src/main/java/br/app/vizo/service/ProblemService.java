package br.app.vizo.service;

import br.app.vizo.controller.response.ProblemDTO;
import br.app.vizo.controller.response.ReportDTO;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.ProblemMapper;
import br.app.vizo.mapper.ReportMapper;
import br.app.vizo.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
