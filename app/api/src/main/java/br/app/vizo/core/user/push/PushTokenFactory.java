package br.app.vizo.core.user.push;

import br.app.vizo.core.user.UserId;

public interface PushTokenFactory {

    PushToken create(UserId userId, String token, Platform platform);
}
