package br.app.vizo.application.usecase.me;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.ReportDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.ReportMapper;
import br.app.vizo.application.usecase.me.filter.ReportFilter;
import br.app.vizo.core.report.ReportRepository;
import br.app.vizo.core.user.User;

import java.util.UUID;

@UseCase
public class GetMyReportsUseCase {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    public GetMyReportsUseCase(ReportRepository reportRepository, ReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    public PageDTO<ReportDTO> execute(User loggedInUser, ReportFilter filter, PaginationDTO pagination) {
        UUID userId = loggedInUser.getId();

        if (filter.latitude() == null || filter.longitude() == null) {
            return this.reportRepository
                    .findAllByUserId(userId, pagination)
                    .map(this.reportMapper::toDto);
        }

        return this.reportRepository.findAllByUserIdWithinDistance(
                userId,
                filter.latitude(),
                filter.longitude(),
                filter.radius(),
                pagination
        ).map(this.reportMapper::toDto);
    }
}
