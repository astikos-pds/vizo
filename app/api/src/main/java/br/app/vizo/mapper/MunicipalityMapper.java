package br.app.vizo.mapper;

import br.app.vizo.controller.response.MunicipalityDTO;
import br.app.vizo.domain.municipality.Municipality;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MunicipalityMapper extends CommonMapper<Municipality, MunicipalityDTO> {

    MunicipalityDTO toDto(Municipality entity);

    Municipality toEntity(MunicipalityDTO dto);
}
