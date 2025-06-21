package br.app.vizo.mapper;

import br.app.vizo.controller.response.OfficialDTO;
import br.app.vizo.domain.user.Official;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OfficialMapper extends CommonMapper<Official, OfficialDTO> {

    @Mapping(source = "municipality.id", target = "municipalityId")
    OfficialDTO toDto(Official entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "municipality", ignore = true)
    Official toEntity(OfficialDTO dto);
}
