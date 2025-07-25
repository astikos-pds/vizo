package br.app.vizo.core.user.password;

import br.app.vizo.core.user.exception.WeakPasswordException;

public class Password {

    private final String value;

    public Password(String value) {
        if (value == null || value.length() < 8) {
            throw new WeakPasswordException("Password must have at least 8 characters.");
        }

        if (!value.matches(".*\\d.*")) {
            throw new WeakPasswordException("Password must have at least one number.");
        }

        if (!value.matches(".*[a-z].*")) {
            throw new WeakPasswordException("Password must have at least one lower case character.");

        }

        if (!value.matches(".*[A-Z].*")) {
            throw new WeakPasswordException("Password must have at least one upper case character.");
        }

        this.value = value;
    }

    public HashedPassword hashWith(PasswordHasher passwordHasher) {
        return new HashedPassword(passwordHasher.hash(this.value));
    }
}
