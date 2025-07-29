package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.mapper.EmailVerificationRequestMapper;
import br.app.vizo.core.verification.EmailVerificationRequest;
import br.app.vizo.core.verification.EmailVerificationRequestRepository;
import br.app.vizo.infrastructure.persistence.jpa.repository.EmailVerificationRequestJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class EmailVerificationRequestRepositoryImpl implements EmailVerificationRequestRepository {

    private final EmailVerificationRequestJpaRepository jpaRepository;
    private final EmailVerificationRequestMapper mapper;

    public EmailVerificationRequestRepositoryImpl(
            EmailVerificationRequestJpaRepository jpaRepository,
            EmailVerificationRequestMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(EmailVerificationRequest emailVerificationRequest) {
        var entity = this.mapper.toEntity(emailVerificationRequest);
        this.jpaRepository.save(entity);
    }

    @Override
    public Optional<EmailVerificationRequest> findById(UUID id) {
        return this.jpaRepository.findById(id).map(this.mapper::toModel);
    }

    @Override
    public Optional<EmailVerificationRequest> findByEmail(String email) {
        return this.jpaRepository.findByEmail(email).map(this.mapper::toModel);
    }
}
