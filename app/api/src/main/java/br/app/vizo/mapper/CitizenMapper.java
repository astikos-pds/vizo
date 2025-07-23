package br.app.vizo.mapper;

import br.app.vizo.domain.user.Citizen;
import br.app.vizo.dto.CitizenDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AvatarMapper.class)
public interface CitizenMapper extends DtoMapper<Citizen, CitizenDTO> {
}
