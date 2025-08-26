package br.app.vizo.application.usecase.poi;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.PointOfInterestDTO;
import br.app.vizo.application.mapper.PointOfInterestMapper;
import br.app.vizo.application.usecase.poi.request.MutatePointOfInterestRequestDTO;
import br.app.vizo.core.poi.PointOfInterest;
import br.app.vizo.core.poi.PointOfInterestRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreatePointOfInterestUseCase {

    private final PointOfInterestRepository pointOfInterestRepository;
    private final PointOfInterestMapper pointOfInterestMapper;

    public PointOfInterestDTO execute(User user, MutatePointOfInterestRequestDTO request) {
        PointOfInterest poi =  user.createPointOfInterest(
                request.name(),
                request.latitude(),
                request.longitude(),
                request.radius()
        );

        PointOfInterest saved = this.pointOfInterestRepository.save(poi);
        return this.pointOfInterestMapper.toDto(saved);
    }
}
