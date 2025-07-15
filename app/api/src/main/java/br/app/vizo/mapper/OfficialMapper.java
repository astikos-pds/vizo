package br.app.vizo.mapper;

import br.app.vizo.controller.response.OfficialDTO;
import br.app.vizo.domain.user.Official;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AvatarMapper.class)
public interface OfficialMapper extends DtoMapper<Official, OfficialDTO> {
}
