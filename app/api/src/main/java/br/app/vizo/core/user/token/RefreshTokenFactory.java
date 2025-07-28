package br.app.vizo.core.user.token;

import br.app.vizo.core.user.UserId;

public interface RefreshTokenFactory {

    RefreshToken create(UserId userId, String token);
}
