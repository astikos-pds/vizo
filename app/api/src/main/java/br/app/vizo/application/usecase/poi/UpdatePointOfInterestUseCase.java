package br.app.vizo.application.usecase.poi;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.PointOfInterestDTO;
import br.app.vizo.application.exception.PointOfInterestNotFoundException;
import br.app.vizo.application.mapper.PointOfInterestMapper;
import br.app.vizo.application.usecase.poi.request.MutatePointOfInterestRequestDTO;
import br.app.vizo.core.poi.PointOfInterest;
import br.app.vizo.core.poi.PointOfInterestRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class UpdatePointOfInterestUseCase {

    private final PointOfInterestRepository pointOfInterestRepository;
    private final PointOfInterestMapper pointOfInterestMapper;

    public PointOfInterestDTO execute(User user, UUID id, MutatePointOfInterestRequestDTO request) {

        PointOfInterest poi = this.pointOfInterestRepository.findById(id)
                .orElseThrow(PointOfInterestNotFoundException::new);

        PointOfInterest updated = user.updatePointOfInterest(
                poi,
                request.name(),
                request.latitude(),
                request.longitude(),
                request.radius()
        );

        PointOfInterest saved = this.pointOfInterestRepository.save(updated);
        return this.pointOfInterestMapper.toDto(saved);
    }
}
