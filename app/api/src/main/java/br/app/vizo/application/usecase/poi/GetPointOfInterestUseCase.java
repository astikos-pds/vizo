package br.app.vizo.application.usecase.poi;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.PointOfInterestDTO;
import br.app.vizo.application.exception.MustBeOwnerException;
import br.app.vizo.application.exception.PointOfInterestNotFoundException;
import br.app.vizo.application.mapper.PointOfInterestMapper;
import br.app.vizo.core.poi.PointOfInterest;
import br.app.vizo.core.poi.PointOfInterestRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetPointOfInterestUseCase {

    private final PointOfInterestRepository pointOfInterestRepository;
    private final PointOfInterestMapper pointOfInterestMapper;

    public PointOfInterestDTO execute(User user, UUID id) {
        PointOfInterest poi =  this.pointOfInterestRepository.findById(id)
                .orElseThrow(PointOfInterestNotFoundException::new);

        if (!user.owns(poi)) {
            throw new MustBeOwnerException();
        }

        return this.pointOfInterestMapper.toDto(poi);
    }
}
