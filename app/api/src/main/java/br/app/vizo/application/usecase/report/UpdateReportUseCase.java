package br.app.vizo.application.usecase.report;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.ReportDTO;
import br.app.vizo.application.exception.ReportDiscrepancyTooBigException;
import br.app.vizo.application.exception.CannotModifyReportException;
import br.app.vizo.application.exception.ReportNotFoundException;
import br.app.vizo.application.mapper.ReportMapper;
import br.app.vizo.application.service.ReportAnalysisService;
import br.app.vizo.application.service.ReportCredibilityService;
import br.app.vizo.application.usecase.report.request.ReportProblemRequestDTO;
import br.app.vizo.core.problem.Problem;
import br.app.vizo.core.problem.ProblemRepository;
import br.app.vizo.core.problem.ProblemStatus;
import br.app.vizo.core.report.Report;
import br.app.vizo.core.report.ReportRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class UpdateReportUseCase {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final ProblemRepository problemRepository;
    private final ReportAnalysisService reportAnalysisService;
    private final ReportCredibilityService reportCredibilityService;

    public ReportDTO execute(User loggedInUser, UUID reportId, ReportProblemRequestDTO request) {

        Report report = this.reportRepository.findById(reportId).orElseThrow(ReportNotFoundException::new);

        if (report.getProblem().getStatus() != ProblemStatus.ANALYSIS) {
            throw new CannotModifyReportException();
        }

        ReportAnalysisService.AnalysisResponse response = this.reportAnalysisService
                .analyseReport(request.description(), request.imagesUrls());

        if (response.predicted_type() != report.getProblem().getType()) {
            throw new ReportDiscrepancyTooBigException();
        }

        int numberOfImages = request.imagesUrls().size();
        Double newReportCredibility = this.reportCredibilityService.calculate(
                loggedInUser.getCredibilityPoints(),
                request.description(),
                numberOfImages,
                false,
                response.text_image_consistency()
        );

        Report updated = loggedInUser.updateReport(
                report,
                request.description(),
                request.latitude(),
                request.longitude(),
                request.imagesUrls(),
                newReportCredibility
        );

        Problem problem = report.getProblem();

        problem.decreaseCredibility(report.getCredibility());
        problem.increaseCredibility(newReportCredibility);

        this.problemRepository.save(problem);

        Report saved = this.reportRepository.save(updated);
        return this.reportMapper.toDto(saved);
    }
}
