package br.app.vizo.core.report;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;

import java.util.UUID;

public interface ReportRepository {

    Report save(Report report);

    PageDTO<Report> findAllByUserId(UUID userId, PaginationDTO pagination);

    PageDTO<Report> findAllByProblemId(UUID problemId, PaginationDTO pagination);

    boolean existsByUserIdAndProblemId(UUID userId, UUID problemId);

    PageDTO<Report> findAllByUserIdWithinDistance(
            UUID userId,
            Double latitude,
            Double longitude,
            Double distance,
            PaginationDTO pagination
    );
}
