package br.app.vizo.mapper;

import br.app.vizo.domain.user.Citizen;
import br.app.vizo.controller.response.CitizenDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AvatarMapper.class)
public interface CitizenMapper extends CommonMapper<Citizen, CitizenDTO> {

    CitizenDTO toDto(Citizen entity);

    @Mapping(target = "password", ignore = true)
    Citizen toEntity(CitizenDTO dto);
}
