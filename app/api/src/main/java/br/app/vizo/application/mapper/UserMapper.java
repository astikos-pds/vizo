package br.app.vizo.application.mapper;

import br.app.vizo.application.dto.UserDTO;
import br.app.vizo.application.Mapper;
import br.app.vizo.application.mapper.base.RepresentationMapper;
import br.app.vizo.core.shared.*;
import br.app.vizo.core.user.Document;
import br.app.vizo.core.user.User;
import br.app.vizo.core.user.UserId;
import br.app.vizo.core.user.password.HashedPassword;
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
                new UserId(entity.getId()),
                new Name(entity.getName()),
                new Document(entity.getDocument()),
                new Email(entity.getEmail()),
                new HashedPassword(entity.getPassword()),
                entity.getAvatarUrl() == null ? null : new Image(entity.getAvatarUrl()),
                new Credibility(entity.getCredibilityPoints()),
                new MutationTimestamps(entity.getCreatedAt(), entity.getUpdatedAt())
        );
    }
}
