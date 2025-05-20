package br.app.vizo.mapper;

import br.app.vizo.domain.user.Citizen;
import br.app.vizo.domain.user.dto.CitizenDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CitizenMapper extends CommonMapper<Citizen, CitizenDTO> {

    CitizenDTO toDto(Citizen entity);

    @Mapping(target = "password", ignore = true)
    Citizen toEntity(CitizenDTO dto);
}
