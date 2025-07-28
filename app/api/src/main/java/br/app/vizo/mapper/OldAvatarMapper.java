package br.app.vizo.mapper;

import br.app.vizo.dto.AvatarDTO;
import br.app.vizo.domain.user.Avatar;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OldAvatarMapper extends DtoMapper<Avatar, AvatarDTO> {
}
