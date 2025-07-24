package br.app.vizo.core.user;

import br.app.vizo.core.user.exception.WeakPasswordException;

public class Password {

    private final String value;

    public Password(String value) {
        if (value == null || value.length() < 8) {
            throw new WeakPasswordException("Password must have at leat 8 characters.");
        }

        if (!value.matches(".*\\d.*")) {
            throw new WeakPasswordException("Password must have at leat one number.");
        }

        if (!value.matches(".*[a-z].*")) {
            throw new WeakPasswordException("Password must have at leat one lower case character.");

        }

        if (!value.matches(".*[A-Z].*")) {
            throw new WeakPasswordException("Password must have at leat one upper case character.");
        }

        this.value = value;
    }

    public HashedPassword hash(PasswordHasher passwordHasher) {
        return new HashedPassword(passwordHasher.hash(this.value));
    }
}
