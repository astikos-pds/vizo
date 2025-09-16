package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.ReportMapper;
import br.app.vizo.core.report.Report;
import br.app.vizo.core.report.ReportRepository;
import br.app.vizo.infrastructure.persistence.jpa.entity.ReportEntity;
import br.app.vizo.infrastructure.persistence.jpa.repository.ReportJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ReportRepositoryImpl implements ReportRepository {

    private final ReportJpaRepository jpaRepository;
    private final ReportMapper mapper;

    public ReportRepositoryImpl(ReportJpaRepository jpaRepository, ReportMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Report save(Report report) {
        ReportEntity entity = this.mapper.toEntity(report);
        ReportEntity saved = this.jpaRepository.save(entity);
        return this.mapper.toModel(saved);
    }

    @Override
    public Optional<Report> findById(UUID id) {
        return this.jpaRepository.findById(id).map(this.mapper::toModel);
    }

    @Override
    public PageDTO<Report> findAllByUserId(UUID userId, PaginationDTO pagination) {
        var page = this.jpaRepository.findAllByUserId(userId, PaginationDTO.resolve(pagination))
                .map(this.mapper::toModel);
        return PageDTO.of(page);
    }

    @Override
    public PageDTO<Report> findAllByProblemId(UUID problemId, PaginationDTO pagination) {
        var page = this.jpaRepository.findAllByProblemId(problemId, PaginationDTO.resolve(pagination))
                .map(this.mapper::toModel);
        return PageDTO.of(page);
    }

    @Override
    public boolean existsByUserIdAndProblemId(UUID userId, UUID problemId) {
        return this.jpaRepository.existsByUserIdAndProblemId(userId, problemId);
    }

    @Override
    public PageDTO<Report> findAllByUserIdWithinDistance(
            UUID userId, Double latitude, Double longitude, Double distance, PaginationDTO pagination
    ) {
        var page = this.jpaRepository.findAllByUserIdWithinDistance(
                userId,
                latitude,
                longitude,
                distance,
                PaginationDTO.resolve(pagination)
        ).map(this.mapper::toModel);

        return PageDTO.of(page);
    }

    @Override
    public void deleteById(UUID id) {
        this.jpaRepository.deleteById(id);
    }
}
