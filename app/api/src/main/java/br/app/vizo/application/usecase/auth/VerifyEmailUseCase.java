package br.app.vizo.application.usecase.auth;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.usecase.auth.request.CodeRequestDTO;
import br.app.vizo.core.verification.EmailVerificationRequestRepository;
import br.app.vizo.exception.NotFoundException;

import java.util.UUID;

@UseCase
public class VerifyEmailUseCase {

    private final EmailVerificationRequestRepository emailVerificationRequestRepository;

    public VerifyEmailUseCase(EmailVerificationRequestRepository emailVerificationRequestRepository) {
        this.emailVerificationRequestRepository = emailVerificationRequestRepository;
    }

    public void execute(UUID id, CodeRequestDTO body) {
        var emailVerificationRequest = this.emailVerificationRequestRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Email verification request not found.")
        );

        emailVerificationRequest.verifyWith(body.code());

        this.emailVerificationRequestRepository.save(emailVerificationRequest);
    }
}
