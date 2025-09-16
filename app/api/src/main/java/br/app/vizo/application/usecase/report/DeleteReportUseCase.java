package br.app.vizo.application.usecase.report;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.exception.CannotModifyReportException;
import br.app.vizo.application.exception.MustBeOwnerException;
import br.app.vizo.application.exception.ReportNotFoundException;
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
public class DeleteReportUseCase {

    private final ReportRepository reportRepository;
    private final ProblemRepository problemRepository;

    public void execute(User loggedInUser, UUID reportId) {

        Report report = this.reportRepository.findById(reportId).orElseThrow(ReportNotFoundException::new);

        if (!loggedInUser.made(report)) {
            throw new MustBeOwnerException();
        }

        if (report.getProblem().getStatus() != ProblemStatus.ANALYSIS) {
            throw new CannotModifyReportException();
        }

        Problem problem = report.getProblem();
        problem.decreaseCredibility(report.getCredibility());

        boolean wasTheOnlyReportForThisProblem = problem.getAccumulatedCredibility() == 0;

        if (wasTheOnlyReportForThisProblem) {
            this.problemRepository.deleteById(problem.getId());
        } else {
            this.problemRepository.save(problem);
        }

        this.reportRepository.deleteById(reportId);
    }
}
