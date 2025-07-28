package br.app.vizo.application.mapper;

import br.app.vizo.application.dto.UserDTO;
import br.app.vizo.application.mapper.base.DtoMapper;
import br.app.vizo.application.mapper.base.EntityMapper;
import br.app.vizo.application.mapper.base.Mapper;
import br.app.vizo.application.mapper.base.ModelMapper;
import br.app.vizo.core.user.User;
import br.app.vizo.infrastructure.persistence.entity.UserEntity;

@Mapper
public class UserMapper implements DtoMapper<User, UserDTO>, EntityMapper<User, UserEntity>, ModelMapper<UserEntity, User> {

    @Override
    public UserDTO toDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getDocument(),
                user.getEmail(),
                user.getName(),
                user.getAvatarUrl(),
                user.getCredibilityPoints(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Override
    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getDocument(),
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getAvatarUrl(),
                user.getCredibilityPoints(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Override
    public User toModel(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getDocument(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getAvatarUrl(),
                entity.getCredibilityPoints(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
