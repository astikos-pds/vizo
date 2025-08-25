package br.app.vizo.application.usecase.auth;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.UserDTO;
import br.app.vizo.application.exception.auth.BadCredentialsException;
import br.app.vizo.application.exception.auth.EmailNeedsToBeVerifiedException;
import br.app.vizo.application.mapper.UserMapper;
import br.app.vizo.application.usecase.auth.request.RegisterRequestDTO;
import br.app.vizo.core.user.User;
import br.app.vizo.core.user.UserFactory;
import br.app.vizo.core.user.UserRepository;
import br.app.vizo.core.verification.EmailVerificationRequestRepository;
import br.app.vizo.core.verification.VerificationPurpose;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RegisterUseCase {

    private final UserFactory userFactory;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final EmailVerificationRequestRepository emailVerificationRequestRepository;

    public UserDTO execute(RegisterRequestDTO body) {
        boolean credentialsAlreadyInUse = this.userRepository.existsByDocumentOrEmail(body.document(), body.email());
        if (credentialsAlreadyInUse) {
            throw new BadCredentialsException();
        }

        boolean isEmailVerified = this.emailVerificationRequestRepository
                .existsByEmailAndPurpose(body.email(), VerificationPurpose.REGISTRATION);
        if (!isEmailVerified) {
            throw new EmailNeedsToBeVerifiedException();
        }

        User user = this.userFactory.create(body.name(), body.document(), body.email(), body.password());
        User saved = this.userRepository.save(user);

        return this.userMapper.toDto(saved);
    }
}
