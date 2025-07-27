package br.app.vizo.core.user.password;

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
