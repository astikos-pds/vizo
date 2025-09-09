package br.app.vizo.application.usecase.poi;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.exception.MustBeOwnerException;
import br.app.vizo.application.exception.PointOfInterestNotFoundException;
import br.app.vizo.core.poi.PointOfInterest;
import br.app.vizo.core.poi.PointOfInterestRepository;
import br.app.vizo.core.user.User;

import java.util.UUID;

@UseCase
public class DeletePointOfInterestUseCase {

    private final PointOfInterestRepository pointOfInterestRepository;

    public DeletePointOfInterestUseCase(PointOfInterestRepository pointOfInterestRepository) {
        this.pointOfInterestRepository = pointOfInterestRepository;
    }

    public void execute(User user, UUID id) {

        PointOfInterest poi = this.pointOfInterestRepository.findById(id)
                .orElseThrow(PointOfInterestNotFoundException::new);

        if (!user.owns(poi)) {
            throw new MustBeOwnerException();
        }

        this.pointOfInterestRepository.deleteById(id);
    }
}
