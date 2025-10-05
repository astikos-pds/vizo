package br.app.vizo.application.usecase.push;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.PushTokenDTO;
import br.app.vizo.application.mapper.PushTokenMapper;
import br.app.vizo.application.usecase.push.request.RegisterPushTokenRequestDTO;
import br.app.vizo.core.user.User;
import br.app.vizo.core.user.UserId;
import br.app.vizo.core.user.push.PushToken;
import br.app.vizo.core.user.push.PushTokenFactory;
import br.app.vizo.core.user.push.PushTokenRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RegisterPushTokenUseCase {

    private final PushTokenRepository pushTokenRepository;
    private final PushTokenMapper pushTokenMapper;
    private final PushTokenFactory pushTokenFactory;

    public PushTokenDTO execute(User loggedInUser, RegisterPushTokenRequestDTO request) {

        PushToken pushToken = this.pushTokenRepository.findByToken(request.token())
                .map(PushToken::use)
                .orElseGet(() -> this.pushTokenFactory.create(
                        new UserId(loggedInUser.getId()),
                        request.token(),
                        request.platform())
                );

        PushToken saved = this.pushTokenRepository.save(pushToken);

        return this.pushTokenMapper.toDto(saved);
    }
}
