package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.PointOfInterestMapper;
import br.app.vizo.core.poi.PointOfInterest;
import br.app.vizo.core.poi.PointOfInterestRepository;
import br.app.vizo.core.shared.coordinates.Coordinates;
import br.app.vizo.infrastructure.persistence.jpa.entity.PointOfInterestEntity;
import br.app.vizo.infrastructure.persistence.jpa.repository.PointOfInterestJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PointOfInterestRepositoryImpl implements PointOfInterestRepository {

    private final PointOfInterestJpaRepository jpaRepository;
    private final PointOfInterestMapper mapper;

    public PointOfInterestRepositoryImpl(PointOfInterestJpaRepository jpaRepository, PointOfInterestMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public PointOfInterest save(PointOfInterest pointOfInterest) {
        PointOfInterestEntity entity = this.mapper.toEntity(pointOfInterest);
        PointOfInterestEntity saved = this.jpaRepository.save(entity);
        return this.mapper.toModel(saved);
    }

    @Override
    public Optional<PointOfInterest> findById(UUID id) {
        return this.jpaRepository.findById(id).map(this.mapper::toModel);
    }

    @Override
    public PageDTO<PointOfInterest> findAllByUserId(UUID id, PaginationDTO pagination) {
        var page = this.jpaRepository.findAllByUserId(id, PaginationDTO.resolve(pagination)).map(this.mapper::toModel);

        return PageDTO.of(page);
    }

    @Override
    public List<PointOfInterest> findAllContaining(Coordinates coordinates) {
        return this.jpaRepository.findAllContaining(coordinates.getLatitude(), coordinates.getLongitude())
                .stream().map(this.mapper::toModel)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        this.jpaRepository.deleteById(id);
    }
}
