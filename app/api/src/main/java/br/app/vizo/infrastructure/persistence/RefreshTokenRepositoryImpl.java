package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.mapper.RefreshTokenMapper;
import br.app.vizo.core.user.token.RefreshToken;
import br.app.vizo.core.user.token.RefreshTokenRepository;
import br.app.vizo.infrastructure.persistence.jpa.entity.RefreshTokenEntity;
import br.app.vizo.infrastructure.persistence.jpa.repository.RefreshTokenJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;
    private final RefreshTokenMapper mapper;

    public RefreshTokenRepositoryImpl(RefreshTokenJpaRepository jpaRepository, RefreshTokenMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = this.mapper.toEntity(refreshToken);
        this.jpaRepository.save(entity);
    }

    @Override
    public boolean existsByTokenAndUserId(String token, UUID userId) {
        return this.jpaRepository.existsByTokenAndUserId(token, userId);
    }

    @Override
    public void deleteByTokenAndUserId(String token, UUID userId) {
        this.jpaRepository.deleteByTokenAndUserId(token, userId);
    }
}
