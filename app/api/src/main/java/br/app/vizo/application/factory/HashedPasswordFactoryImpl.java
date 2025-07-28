package br.app.vizo.application.factory;

import br.app.vizo.application.Factory;
import br.app.vizo.core.user.password.HashedPassword;
import br.app.vizo.core.user.password.HashedPasswordFactory;
import br.app.vizo.core.user.password.Password;
import br.app.vizo.core.user.password.PasswordHasher;

@Factory
public class HashedPasswordFactoryImpl implements HashedPasswordFactory {

    private final PasswordHasher passwordHasher;

    public HashedPasswordFactoryImpl(PasswordHasher passwordHasher) {
        this.passwordHasher = passwordHasher;
    }

    @Override
    public HashedPassword create(String password) {
        return new Password(password).hashWith(passwordHasher);
    }
}
