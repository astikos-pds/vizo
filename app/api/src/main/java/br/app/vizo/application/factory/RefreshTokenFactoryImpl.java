package br.app.vizo.application.factory;

import br.app.vizo.core.shared.ExpirationTimestamp;
import br.app.vizo.core.user.User;
import br.app.vizo.core.user.token.RefreshToken;
import br.app.vizo.core.user.token.RefreshTokenFactory;

@Factory
public class RefreshTokenFactoryImpl implements RefreshTokenFactory {

    private static final int A_MONTH_IN_HOURS = 24 * 30;

    @Override
    public RefreshToken create(User user, String token) {
        return new RefreshToken(user, token, ExpirationTimestamp.fromNowPlusHours(A_MONTH_IN_HOURS));
    }
}
