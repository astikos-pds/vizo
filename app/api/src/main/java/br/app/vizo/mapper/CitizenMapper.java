package br.app.vizo.mapper;

import br.app.vizo.domain.user.Citizen;
import br.app.vizo.controller.response.CitizenDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AvatarMapper.class)
public interface CitizenMapper extends DtoMapper<Citizen, CitizenDTO> {
}
