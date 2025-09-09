package br.app.vizo.core.poi;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.core.shared.coordinates.Coordinates;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PointOfInterestRepository {

    PointOfInterest save(PointOfInterest pointOfInterest);

    Optional<PointOfInterest> findById(UUID id);

    PageDTO<PointOfInterest> findAllByUserId(UUID id, PaginationDTO pagination);

    List<PointOfInterest> findAllContaining(Coordinates coordinates);

    void deleteById(UUID id);
}
