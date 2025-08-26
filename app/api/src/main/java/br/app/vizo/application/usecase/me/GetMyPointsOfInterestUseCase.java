package br.app.vizo.application.usecase.me;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.PointOfInterestDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.PointOfInterestMapper;
import br.app.vizo.core.poi.PointOfInterestRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetMyPointsOfInterestUseCase {

    private final PointOfInterestRepository pointOfInterestRepository;
    private final PointOfInterestMapper pointOfInterestMapper;

    public PageDTO<PointOfInterestDTO> execute(User user, PaginationDTO pagination) {
        return this.pointOfInterestRepository.findAllByUserId(user.getId(), pagination)
                .map(this.pointOfInterestMapper::toDto);
    }
}
