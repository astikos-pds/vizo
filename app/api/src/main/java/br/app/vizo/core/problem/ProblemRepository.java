package br.app.vizo.core.problem;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProblemRepository {

    Problem save(Problem problem);

    boolean existsById(UUID id);

    Optional<Problem> findById(UUID id);

    List<Problem> findAll();

    PageDTO<Problem> findAllByTypeIn(Set<ProblemType> types, PaginationDTO pagination);

    Optional<Problem> findClosestUnresolvedByTypeWithinRadiusInMeters(
            ProblemType problemType,
            Double latitude,
            Double longitude,
            Double radiusInMeters
    );

    List<ProblemStatistics> countByRangeAndStatusesAndTypes(
            LocalDate start,
            LocalDate end,
            Set<ProblemStatus> statuses,
            Set<ProblemType> types
    );

    List<ProblemCountByStatus> countByStatusAndTypeIn(Set<ProblemType> types);

    void deleteById(UUID id);
}
