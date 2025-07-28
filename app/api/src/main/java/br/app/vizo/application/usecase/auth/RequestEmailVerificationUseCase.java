package br.app.vizo.application.usecase.auth;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.EmailVerificationDTO;
import br.app.vizo.application.mapper.EmailVerificationRequestMapper;
import br.app.vizo.application.service.EmailService;
import br.app.vizo.application.usecase.auth.request.EmailRequestDTO;
import br.app.vizo.core.verification.EmailVerificationRequest;
import br.app.vizo.core.verification.EmailVerificationRequestFactory;
import br.app.vizo.infrastructure.persistence.EmailVerificationRequestRepository;
import br.app.vizo.infrastructure.persistence.entity.EmailVerificationRequestEntity;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public class RequestEmailVerificationUseCase {

    private final EmailVerificationRequestRepository emailVerificationRequestRepository;
    private final EmailVerificationRequestFactory emailVerificationRequestFactory;
    private final EmailVerificationRequestMapper emailVerificationRequestMapper;
    private final EmailService emailService;

    public EmailVerificationDTO execute(EmailRequestDTO body) {
        Optional<EmailVerificationRequestEntity> entity = this.emailVerificationRequestRepository.findByEmail(body.email());

        EmailVerificationRequest emailVerificationRequest = entity
                .map(emailVerificationRequestMapper::toModel)
                .map(request -> {
                    request.retry();
                    return request;
                })
                .orElseGet(() -> this.emailVerificationRequestFactory.create(body.email()));

        this.emailService.sendVerificationCode(
                emailVerificationRequest.getEmail(),
                emailVerificationRequest.getCode(),
                emailVerificationRequest.getExpiresAt()
        );

        EmailVerificationRequestEntity newEntity = this.emailVerificationRequestMapper.toEntity(emailVerificationRequest);
        this.emailVerificationRequestRepository.save(newEntity);

        return this.emailVerificationRequestMapper.toDto(emailVerificationRequest);
    }
}
