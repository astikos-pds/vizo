package br.app.vizo.application.usecase.problem;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.ReportDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.exception.ProblemNotFoundException;
import br.app.vizo.application.mapper.ReportMapper;
import br.app.vizo.core.problem.ProblemRepository;
import br.app.vizo.core.report.ReportRepository;

import java.util.UUID;

@UseCase
public class GetReportsForProblemUseCase {

    private final ProblemRepository problemRepository;
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    public GetReportsForProblemUseCase(ProblemRepository problemRepository, ReportRepository reportRepository, ReportMapper reportMapper) {
        this.problemRepository = problemRepository;
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    public PageDTO<ReportDTO> execute(UUID problemId, PaginationDTO pagination) {
        boolean problemExists = this.problemRepository.existsById(problemId);
        if (!problemExists) {
            throw new ProblemNotFoundException();
        }

        return this.reportRepository.findAllByProblemId(problemId, pagination)
                .map(this.reportMapper::toDto);
    }
}
