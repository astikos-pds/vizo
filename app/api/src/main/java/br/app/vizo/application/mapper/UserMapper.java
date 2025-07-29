package br.app.vizo.application.mapper;

import br.app.vizo.application.dto.UserDTO;
import br.app.vizo.application.Mapper;
import br.app.vizo.application.mapper.base.RepresentationMapper;
import br.app.vizo.core.user.User;
import br.app.vizo.infrastructure.persistence.jpa.entity.UserEntity;

@Mapper
public class UserMapper implements RepresentationMapper<User, UserEntity, UserDTO> {

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
