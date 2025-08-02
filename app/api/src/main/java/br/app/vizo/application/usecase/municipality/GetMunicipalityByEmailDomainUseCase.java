package br.app.vizo.application.usecase.municipality;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.MunicipalityDTO;
import br.app.vizo.application.exception.MunicipalityNotFoundException;
import br.app.vizo.application.mapper.MunicipalityMapper;
import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.municipality.MunicipalityRepository;

@UseCase
public class GetMunicipalityByEmailDomainUseCase {

    private final MunicipalityRepository municipalityRepository;
    private final MunicipalityMapper municipalityMapper;

    public GetMunicipalityByEmailDomainUseCase(MunicipalityRepository municipalityRepository, MunicipalityMapper municipalityMapper) {
        this.municipalityRepository = municipalityRepository;
        this.municipalityMapper = municipalityMapper;
    }

    public MunicipalityDTO execute(String emailDomain) {
        Municipality municipality = this.municipalityRepository.findByEmailDomain(emailDomain)
                .orElseThrow(MunicipalityNotFoundException::new);

        return this.municipalityMapper.toDto(municipality);
    }

}
