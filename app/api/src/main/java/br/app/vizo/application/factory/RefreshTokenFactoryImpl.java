package br.app.vizo.application.factory;

import br.app.vizo.application.Factory;
import br.app.vizo.core.shared.ExpirationTimestamp;
import br.app.vizo.core.user.UserId;
import br.app.vizo.core.user.token.RefreshToken;
import br.app.vizo.core.user.token.RefreshTokenFactory;

@Factory
public class RefreshTokenFactoryImpl implements RefreshTokenFactory {

    private static final int A_MONTH_IN_HOURS = 24 * 30;

    @Override
    public RefreshToken create(UserId userId, String token) {
        return new RefreshToken(userId, token, ExpirationTimestamp.fromNowPlusHours(A_MONTH_IN_HOURS));
    }
}
