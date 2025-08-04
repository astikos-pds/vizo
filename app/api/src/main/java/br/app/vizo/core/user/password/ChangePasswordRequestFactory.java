package br.app.vizo.core.user.password;

import br.app.vizo.core.user.User;

public interface ChangePasswordRequestFactory {

    public ChangePasswordRequest create(User user);
}
