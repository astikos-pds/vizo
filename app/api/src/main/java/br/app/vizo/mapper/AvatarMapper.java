package br.app.vizo.mapper;

import br.app.vizo.controller.response.AvatarDTO;
import br.app.vizo.domain.user.avatar.Avatar;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AvatarMapper extends DtoMapper<Avatar, AvatarDTO> {
}
