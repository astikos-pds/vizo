package br.app.vizo.core.problem;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProblemRepository {

    Problem save(Problem problem);

    boolean existsById(UUID id);

    Optional<Problem> findById(UUID id);

    Iterable<Problem> findAll();

    PageDTO<Problem> findAllByTypeIn(Set<ProblemType> types, PaginationDTO pagination);

    Optional<Problem> findNearestWithinDistance(Double latitude, Double longitude, Double distance);
}
