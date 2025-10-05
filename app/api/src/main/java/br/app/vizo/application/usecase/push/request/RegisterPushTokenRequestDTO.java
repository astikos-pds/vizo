package br.app.vizo.application.usecase.push.request;

import br.app.vizo.core.user.push.Platform;

public record RegisterPushTokenRequestDTO(
        String token,
        Platform platform
) {
}
