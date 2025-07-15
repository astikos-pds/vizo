package br.app.vizo.mapper;

import br.app.vizo.controller.response.OfficialDTO;
import br.app.vizo.domain.user.Official;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AvatarMapper.class)
public interface OfficialMapper extends CommonMapper<Official, OfficialDTO> {

    OfficialDTO toDto(Official entity);

    @Mapping(target = "password", ignore = true)
    Official toEntity(OfficialDTO dto);
}
