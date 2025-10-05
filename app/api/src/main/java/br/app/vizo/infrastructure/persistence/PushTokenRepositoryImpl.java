package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.mapper.PushTokenMapper;
import br.app.vizo.core.user.push.PushToken;
import br.app.vizo.core.user.push.PushTokenRepository;
import br.app.vizo.infrastructure.persistence.jpa.repository.PushTokenJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class PushTokenRepositoryImpl implements PushTokenRepository {

    private final PushTokenJpaRepository jpaRepository;
    private final PushTokenMapper mapper;

    public PushTokenRepositoryImpl(PushTokenJpaRepository jpaRepository, PushTokenMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<PushToken> findByToken(String token) {
        return this.jpaRepository.findByToken(token).map(this.mapper::toModel);
    }

    @Override
    public List<PushToken> findAllByUserId(UUID id) {
        return this.jpaRepository.findAllByUserId(id).stream()
                .map(this.mapper::toModel).collect(Collectors.toList());
    }
}
