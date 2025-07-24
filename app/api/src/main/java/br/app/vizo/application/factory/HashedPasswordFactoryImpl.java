package br.app.vizo.application.factory;

import br.app.vizo.core.user.password.HashedPassword;
import br.app.vizo.core.user.password.HashedPasswordFactory;
import br.app.vizo.core.user.password.Password;
import br.app.vizo.core.user.password.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HashedPasswordFactoryImpl implements HashedPasswordFactory {

    private final PasswordHasher passwordHasher;

    public HashedPassword create(String password) {
        return new Password(password).hashWith(passwordHasher);
    }
}
