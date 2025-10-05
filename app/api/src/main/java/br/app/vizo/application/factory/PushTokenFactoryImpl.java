package br.app.vizo.application.factory;

import br.app.vizo.application.Factory;
import br.app.vizo.core.user.UserId;
import br.app.vizo.core.user.push.Platform;
import br.app.vizo.core.user.push.PushToken;
import br.app.vizo.core.user.push.PushTokenFactory;

@Factory
public class PushTokenFactoryImpl implements PushTokenFactory {

    @Override
    public PushToken create(UserId userId, String token, Platform platform) {
        return new PushToken(userId, token, platform);
    }
}
