package br.app.vizo.core.user.token;

import br.app.vizo.core.shared.ExpirationTimestamp;
import br.app.vizo.core.user.User;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenFactoryImpl implements RefreshTokenFactory {

    @Override
    public RefreshToken create(User user, String token, int expirationTimeInHours) {
        return new RefreshToken(user, token, ExpirationTimestamp.fromNowPlusHours(expirationTimeInHours));
    }
}
