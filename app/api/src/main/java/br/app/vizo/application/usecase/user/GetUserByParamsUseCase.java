package br.app.vizo.application.usecase.user;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.UserDTO;
import br.app.vizo.application.exception.auth.UserNotFoundException;
import br.app.vizo.application.mapper.UserMapper;
import br.app.vizo.application.usecase.user.filter.GetUserParamsDTO;
import br.app.vizo.core.user.UserRepository;

@UseCase
public class GetUserByParamsUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public GetUserByParamsUseCase(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO execute(GetUserParamsDTO params) {
        if (params.document() == null) {
            return this.userRepository.findByEmail(params.getEmail())
                    .map(this.userMapper::toDto)
                    .orElseThrow(UserNotFoundException::new);
        }

        return this.userRepository.findByDocumentAndEmail(params.getDocument(), params.getEmail())
                .map(this.userMapper::toDto)
                .orElseThrow(UserNotFoundException::new);
    }
}
