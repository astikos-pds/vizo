package br.app.vizo.application.usecase.me;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.UserDTO;
import br.app.vizo.application.mapper.UserMapper;
import br.app.vizo.core.user.User;

@UseCase
public class GetMeUseCase {

    private final UserMapper userMapper;

    public GetMeUseCase(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UserDTO execute(User loggedInUser) {
        return this.userMapper.toDto(loggedInUser);
    }
}
