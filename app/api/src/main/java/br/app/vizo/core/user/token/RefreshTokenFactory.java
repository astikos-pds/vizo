package br.app.vizo.core.user.token;

import br.app.vizo.core.user.User;

public interface RefreshTokenFactory {

    RefreshToken create(User user, String token);
}
