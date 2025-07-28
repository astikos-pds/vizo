package br.app.vizo.core.user.password;

import br.app.vizo.core.user.exception.InvalidHashedPasswordException;
import jakarta.persistence.Column;

public record HashedPassword(
        @Column(name = "password", nullable = false) String value
) {

    public HashedPassword {
        if (value == null || value.isBlank()) {
            throw new InvalidHashedPasswordException();
        }
    }

    public boolean matches(String rawPassword, PasswordHasher passwordHasher) {
        return passwordHasher.matches(rawPassword, this.value);
    }
}
