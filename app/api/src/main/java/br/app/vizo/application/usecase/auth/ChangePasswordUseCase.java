package br.app.vizo.application.usecase.auth;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.exception.auth.EmailNeedsToBeVerifiedException;
import br.app.vizo.application.exception.auth.UserNotFoundException;
import br.app.vizo.application.usecase.auth.request.ChangePasswordRequestDTO;
import br.app.vizo.core.user.User;
import br.app.vizo.core.user.UserRepository;
import br.app.vizo.core.user.password.HashedPassword;
import br.app.vizo.core.user.password.HashedPasswordFactory;
import br.app.vizo.core.verification.EmailVerificationRequest;
import br.app.vizo.core.verification.EmailVerificationRequestRepository;
import br.app.vizo.core.verification.VerificationPurpose;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final EmailVerificationRequestRepository emailVerificationRequestRepository;
    private final HashedPasswordFactory hashedPasswordFactory;

    public void execute(ChangePasswordRequestDTO request) {
        EmailVerificationRequest verificationRequest = this.ensureEmailWasVerified(request.email());

        User user = this.userRepository.findByEmail(request.email())
                .orElseThrow(UserNotFoundException::new);

        HashedPassword newHashedPassword = this.hashedPasswordFactory.create(request.newPassword());
        user.changePassword(newHashedPassword);

        this.userRepository.save(user);
        this.emailVerificationRequestRepository.deleteById(verificationRequest.getId());
    }

    private EmailVerificationRequest ensureEmailWasVerified(String email) {
        var verificationRequest = this.emailVerificationRequestRepository
                .findByEmailAndPurpose(email, VerificationPurpose.PASSWORD_CHANGE)
                .orElseThrow(EmailNeedsToBeVerifiedException::new);

        if (!verificationRequest.isVerified()) {
            throw new EmailNeedsToBeVerifiedException();
        }

        return verificationRequest;
    }
}
