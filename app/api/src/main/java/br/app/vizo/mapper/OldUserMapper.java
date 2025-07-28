package br.app.vizo.mapper;

import br.app.vizo.domain.user.User;
import br.app.vizo.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OldUserMapper extends DtoMapper<User, UserDTO> {
}
