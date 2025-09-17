package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.ProblemMapper;
import br.app.vizo.core.problem.*;
import br.app.vizo.infrastructure.persistence.jpa.entity.ProblemEntity;
import br.app.vizo.infrastructure.persistence.jpa.repository.ProblemJpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class ProblemRepositoryImpl implements ProblemRepository {

    private final ProblemJpaRepository jpaRepository;
    private final ProblemMapper mapper;

    public ProblemRepositoryImpl(ProblemJpaRepository jpaRepository, ProblemMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Problem save(Problem problem) {
        ProblemEntity entity = this.mapper.toEntity(problem);
        ProblemEntity saved = this.jpaRepository.save(entity);
        return this.mapper.toModel(saved);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.jpaRepository.existsById(id);
    }

    @Override
    public Optional<Problem> findById(UUID id) {
        return this.jpaRepository.findById(id).map(this.mapper::toModel);
    }

    @Override
    public List<Problem> findAll() {
        return this.jpaRepository.findAll().stream().map(this.mapper::toModel).toList();
    }

    @Override
    public PageDTO<Problem> findAllByTypeIn(Set<ProblemType> types, PaginationDTO pagination) {
        var page = this.jpaRepository.findAllByTypeIn(types, PaginationDTO.resolve(pagination))
                .map(this.mapper::toModel);

        return PageDTO.of(page);
    }

    @Override
    public Optional<Problem> findClosestUnresolvedByTypeWithinRadiusInMeters(
            ProblemType problemType,
            Double latitude,
            Double longitude,
            Double radiusInMeters
    ) {
        return this.jpaRepository
                .findClosestUnresolvedByTypeWithinRadiusInMeters(problemType.name(), latitude, longitude, radiusInMeters)
                .map(this.mapper::toModel);
    }

    @Override
    public List<ProblemStatistics> countByRangeAndStatusesAndTypes(
            LocalDate start,
            LocalDate end,
            Set<ProblemStatus> statuses,
            Set<ProblemType> types
    ) {
        return this.jpaRepository.countByRangeAndStatusesAndTypes(start, end, statuses, types)
                .stream()
                .map((o) -> {
                    Date sqlDate = (Date) o[0];
                    return new ProblemStatistics(sqlDate.toLocalDate(), (Long) o[1]);
                })
                .toList();
    }


    @Override
    public List<ProblemCountByStatus> countByStatusAndTypeIn(Set<ProblemType> types) {
        return this.jpaRepository.countByStatusAndTypeIn(types)
                .stream()
                .map((o) -> new ProblemCountByStatus((ProblemStatus) o[0], (Long) o[1]))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        this.jpaRepository.deleteById(id);
    }
}
