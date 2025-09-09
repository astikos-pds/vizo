package br.app.vizo.application.usecase.auth;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.EmailVerificationDTO;
import br.app.vizo.application.exception.EmailAlreadyInUseException;
import br.app.vizo.application.exception.InstitutionalEmailAlreadyInUse;
import br.app.vizo.application.exception.auth.UserNotFoundException;
import br.app.vizo.application.mapper.EmailVerificationRequestMapper;
import br.app.vizo.application.service.EmailService;
import br.app.vizo.application.usecase.auth.request.EmailRequestDTO;
import br.app.vizo.core.affiliation.AffiliatedUserRepository;
import br.app.vizo.core.user.UserRepository;
import br.app.vizo.core.verification.EmailVerificationRequest;
import br.app.vizo.core.verification.EmailVerificationRequestFactory;
import br.app.vizo.core.verification.EmailVerificationRequestRepository;
import br.app.vizo.core.verification.VerificationPurpose;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RequestEmailVerificationUseCase {

    private final UserRepository userRepository;
    private final AffiliatedUserRepository affiliatedUserRepository;
    private final EmailVerificationRequestRepository emailVerificationRequestRepository;
    private final EmailVerificationRequestFactory emailVerificationRequestFactory;
    private final EmailVerificationRequestMapper emailVerificationRequestMapper;
    private final EmailService emailService;

    public EmailVerificationDTO execute(EmailRequestDTO body) {

        if (body.purpose() == VerificationPurpose.REGISTRATION) {
            this.userRepository.findByEmail(body.email()).ifPresent(u -> {
                throw new EmailAlreadyInUseException();
            });
        }

        if (body.purpose() == VerificationPurpose.PASSWORD_CHANGE) {
            this.userRepository.findByEmail(body.email()).orElseThrow(UserNotFoundException::new);
        }

        if (body.purpose() == VerificationPurpose.AFFILIATION) {
            boolean institutionalEmailAlreadyInUse = this.affiliatedUserRepository.existsByInstitutionalEmail(body.email());
            if (institutionalEmailAlreadyInUse) {
                throw new InstitutionalEmailAlreadyInUse();
            }
        }

        EmailVerificationRequest emailVerificationRequest = this.emailVerificationRequestRepository
                .findByEmailAndPurpose(body.email(), body.purpose())
                .map(request -> {
                    request.retry();
                    return request;
                })
                .orElseGet(() -> this.emailVerificationRequestFactory.create(body.email(), body.purpose()));

        this.emailService.sendVerificationCode(
                emailVerificationRequest.getEmail(),
                emailVerificationRequest.getCode(),
                emailVerificationRequest.getExpiresAt()
        );

        this.emailVerificationRequestRepository.save(emailVerificationRequest);

        return this.emailVerificationRequestMapper.toDto(emailVerificationRequest);
    }
}
