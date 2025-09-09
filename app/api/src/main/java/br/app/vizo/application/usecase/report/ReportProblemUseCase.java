package br.app.vizo.application.usecase.report;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.ReportDTO;
import br.app.vizo.application.mapper.ReportMapper;
import br.app.vizo.application.service.ReportCredibilityService;
import br.app.vizo.application.usecase.report.request.ReportProblemRequestDTO;
import br.app.vizo.core.problem.Problem;
import br.app.vizo.core.problem.ProblemFactory;
import br.app.vizo.core.problem.ProblemRepository;
import br.app.vizo.core.problem.ProblemType;
import br.app.vizo.core.report.Report;
import br.app.vizo.core.report.ReportRepository;
import br.app.vizo.core.shared.coordinates.Coordinates;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ReportProblemUseCase {

    private final ProblemRepository problemRepository;
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final ProblemFactory problemFactory;
    private final ReportCredibilityService reportCredibilityService;

    public ReportDTO execute(User loggedInUser, ReportProblemRequestDTO request) {
        Problem problem = this.findRelatedOrCreateProblem(request);

        int numberOfImages = request.imagesUrls().size();
        boolean userAlreadyReportedProblem = this.reportRepository.existsByUserIdAndProblemId(
                loggedInUser.getId(),
                problem.getId()
        );

        Double reportCredibility = this.reportCredibilityService.calculate(
                loggedInUser.getCredibilityPoints(),
                request.description(),
                numberOfImages,
                userAlreadyReportedProblem
        );

        System.out.println(reportCredibility);

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

        return this.reportMapper.toDto(saved);
    }

    private Problem findRelatedOrCreateProblem(ReportProblemRequestDTO request) {
        ProblemType problemType = request.problemType();
        Double latitude = request.latitude();
        Double longitude = request.longitude();

        return this.problemRepository
                .findClosestUnresolvedByTypeWithinRadiusInMeters(problemType, latitude, longitude, 5.0)
                .orElseGet(() -> problemFactory.create(Coordinates.of(latitude, longitude), problemType)
        );
    }
}
