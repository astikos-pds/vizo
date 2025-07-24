package br.app.vizo.core.user.password;

import br.app.vizo.core.user.exception.InvalidHashedPasswordException;

public class HashedPassword {

    private final String value;

    public HashedPassword(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidHashedPasswordException();
        }
        this.value = value;
    }

    public boolean matches(String rawPassword, PasswordHasher passwordHasher) {
        return passwordHasher.matches(rawPassword, this.value);
    }
}
