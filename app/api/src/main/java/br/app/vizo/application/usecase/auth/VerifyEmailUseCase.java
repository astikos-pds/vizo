package br.app.vizo.application.usecase.auth;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.mapper.EmailVerificationRequestMapper;
import br.app.vizo.application.usecase.auth.request.CodeRequestDTO;
import br.app.vizo.core.verification.EmailVerificationRequest;
import br.app.vizo.exception.NotFoundException;
import br.app.vizo.infrastructure.persistence.EmailVerificationRequestRepository;
import br.app.vizo.infrastructure.persistence.entity.EmailVerificationRequestEntity;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class VerifyEmailUseCase {

    private final EmailVerificationRequestRepository emailVerificationRequestRepository;
    private final EmailVerificationRequestMapper emailVerificationRequestMapper;

    public void execute(UUID id, CodeRequestDTO body) {
        var entity = this.emailVerificationRequestRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Email verification request not found.")
        );

        EmailVerificationRequest emailVerificationRequest = this.emailVerificationRequestMapper.toModel(entity);

        emailVerificationRequest.verifyWith(body.code());

        var updatedEntity = this.emailVerificationRequestMapper.toEntity(emailVerificationRequest);
        this.emailVerificationRequestRepository.save(updatedEntity);
    }
}
