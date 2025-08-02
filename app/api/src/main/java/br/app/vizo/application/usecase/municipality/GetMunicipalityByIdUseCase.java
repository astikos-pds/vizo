package br.app.vizo.application.usecase.municipality;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.MunicipalityDTO;
import br.app.vizo.application.exception.MunicipalityNotFoundException;
import br.app.vizo.application.mapper.MunicipalityMapper;
import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.municipality.MunicipalityRepository;

import java.util.UUID;

@UseCase
public class GetMunicipalityByIdUseCase {

    private final MunicipalityRepository municipalityRepository;
    private final MunicipalityMapper municipalityMapper;

    public GetMunicipalityByIdUseCase(MunicipalityRepository municipalityRepository, MunicipalityMapper municipalityMapper) {
        this.municipalityRepository = municipalityRepository;
        this.municipalityMapper = municipalityMapper;
    }

    public MunicipalityDTO execute(UUID municipalityId) {
        Municipality municipality = this.municipalityRepository.findById(municipalityId)
                .orElseThrow(MunicipalityNotFoundException::new);

        return this.municipalityMapper.toDto(municipality);
    }

}
