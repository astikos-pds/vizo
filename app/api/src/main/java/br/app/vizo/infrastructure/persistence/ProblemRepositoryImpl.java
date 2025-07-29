package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.ProblemMapper;
import br.app.vizo.core.problem.Problem;
import br.app.vizo.core.problem.ProblemRepository;
import br.app.vizo.core.problem.ProblemType;
import br.app.vizo.infrastructure.persistence.jpa.entity.ProblemEntity;
import br.app.vizo.infrastructure.persistence.jpa.repository.ProblemJpaRepository;
import org.springframework.stereotype.Repository;

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
    public Iterable<Problem> findAll() {
        return this.jpaRepository.findAll().stream().map(this.mapper::toModel).toList();
    }

    @Override
    public PageDTO<Problem> findAllByTypeIn(Set<ProblemType> types, PaginationDTO pagination) {
        var page = this.jpaRepository.findAllByTypeIn(types, PaginationDTO.resolve(pagination))
                .map(this.mapper::toModel);

        return PageDTO.of(page);
    }

    @Override
    public Optional<Problem> findNearestWithinDistance(Double latitude, Double longitude, Double distance) {
        return this.jpaRepository
                .findNearestWithinDistance(latitude, longitude, distance)
                .map(this.mapper::toModel);
    }
}
