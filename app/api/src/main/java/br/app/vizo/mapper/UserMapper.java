package br.app.vizo.mapper;

import br.app.vizo.domain.user.User;
import br.app.vizo.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends DtoMapper<User, UserDTO> {
}
