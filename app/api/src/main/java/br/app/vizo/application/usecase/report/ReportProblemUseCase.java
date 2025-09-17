package br.app.vizo.application.usecase.report;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.ReportDTO;
import br.app.vizo.application.exception.LowReportConfidenceException;
import br.app.vizo.application.mapper.ReportMapper;
import br.app.vizo.application.service.ReportAnalysisService;
import br.app.vizo.application.service.ReportCredibilityService;
import br.app.vizo.application.usecase.report.request.ReportProblemRequestDTO;
import br.app.vizo.core.notification.event.NewProblemEvent;
import br.app.vizo.core.problem.Problem;
import br.app.vizo.core.problem.ProblemFactory;
import br.app.vizo.core.problem.ProblemRepository;
import br.app.vizo.core.problem.ProblemType;
import br.app.vizo.core.report.Report;
import br.app.vizo.core.report.ReportRepository;
import br.app.vizo.core.shared.coordinates.Coordinates;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;

@UseCase
@RequiredArgsConstructor
public class ReportProblemUseCase {

    private final ProblemRepository problemRepository;
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final ProblemFactory problemFactory;
    private final ReportCredibilityService reportCredibilityService;
    private final ReportAnalysisService reportAnalysisService;
    private final ApplicationEventPublisher eventPublisher;

    public ReportDTO execute(User loggedInUser, ReportProblemRequestDTO request) {
        ReportAnalysisService.AnalysisResponse response = this.reportAnalysisService
                .analyseReport(request.description(), request.imagesUrls());

        if (response.confidence() < 0.3) {
            throw new LowReportConfidenceException();
        }

        Problem problem = this.findRelatedOrCreateProblem(
                response.predicted_type(),
                request.latitude(),
                request.longitude()
        );

        boolean userAlreadyReportedProblem = this.reportRepository.existsByUserIdAndProblemId(
                loggedInUser.getId(),
                problem.getId()
        );

        int numberOfImages = request.imagesUrls().size();

        Double reportCredibility = this.reportCredibilityService.calculate(
                loggedInUser.getCredibilityPoints(),
                request.description(),
                numberOfImages,
                userAlreadyReportedProblem,
                response.text_image_consistency()
        );

        Report report = loggedInUser.report(
                problem,
                request.description(),
                request.latitude(),
                request.longitude(),
                request.imagesUrls(),
                reportCredibility
        );

        problem.increaseCredibility(report.getCredibility());

        this.problemRepository.save(problem);
        Report saved = this.reportRepository.save(report);

        this.eventPublisher.publishEvent(
                new NewProblemEvent(
                        problem.getId(),
                        problem.getType(),
                        problem.getLatitude(),
                        problem.getLongitude(),
                        report.getId(),
                        report.getDescription(),
                        Instant.now()

                )
        );

        return this.reportMapper.toDto(saved);
    }

    private Problem findRelatedOrCreateProblem(ProblemType problemType, Double latitude, Double longitude) {
        return this.problemRepository
                .findClosestUnresolvedByTypeWithinRadiusInMeters(problemType, latitude, longitude, 5.0)
                .orElseGet(() -> this.problemFactory.create(Coordinates.of(latitude, longitude), problemType));
    }
}
