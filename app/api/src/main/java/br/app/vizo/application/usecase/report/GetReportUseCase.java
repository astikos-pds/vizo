package br.app.vizo.application.usecase.report;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.ReportDTO;
import br.app.vizo.application.exception.NotAllowedException;
import br.app.vizo.application.exception.ReportNotFoundException;
import br.app.vizo.application.mapper.ReportMapper;
import br.app.vizo.core.report.Report;
import br.app.vizo.core.report.ReportRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetReportUseCase {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    public ReportDTO execute(User loggedInUser, UUID id) {
        Report report = this.reportRepository.findById(id).orElseThrow(ReportNotFoundException::new);

        if (!loggedInUser.made(report)) {
            throw new NotAllowedException();
        }

        return this.reportMapper.toDto(report);
    }
}
