package br.app.vizo.application.factory;

import br.app.vizo.core.shared.ExpirationTimestamp;
import br.app.vizo.core.user.User;
import br.app.vizo.core.user.token.RefreshToken;
import br.app.vizo.core.user.token.RefreshTokenFactory;

@Factory
public class RefreshTokenFactoryImpl implements RefreshTokenFactory {

    @Override
    public RefreshToken create(User user, String token, int expirationTimeInHours) {
        return new RefreshToken(user, token, ExpirationTimestamp.fromNowPlusHours(expirationTimeInHours));
    }
}
