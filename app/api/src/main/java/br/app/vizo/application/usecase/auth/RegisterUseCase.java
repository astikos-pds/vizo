package br.app.vizo.application.usecase.auth;

import br.app.vizo.application.dto.UserDTO;
import br.app.vizo.application.mapper.UserMapper;
import br.app.vizo.application.UseCase;
import br.app.vizo.application.usecase.auth.request.RegisterRequestDTO;
import br.app.vizo.core.user.User;
import br.app.vizo.core.user.UserFactory;
import br.app.vizo.exception.UnauthorizedException;
import br.app.vizo.infrastructure.persistence.UserRepository;
import br.app.vizo.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RegisterUseCase {

    private final UserFactory userFactory;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserDTO execute(RegisterRequestDTO body) {
        boolean credentialsAlreadyInUse = this.userRepository.existsByDocumentOrEmail(body.document(), body.email());
        if (credentialsAlreadyInUse) {
            throw new UnauthorizedException("Invalid credentials");
        }

        User user = this.userFactory.create(body.name(), body.document(), body.email(), body.password());

        UserEntity userEntity = this.userMapper.toEntity(user);
        this.userRepository.save(userEntity);

        return this.userMapper.toDto(user);
    }
}
