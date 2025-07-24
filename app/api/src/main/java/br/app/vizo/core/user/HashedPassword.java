package br.app.vizo.core.user;

public record HashedPassword(String value) {

    public boolean matches(String rawPassword, PasswordHasher passwordHasher) {
        return passwordHasher.matches(rawPassword, this.value);
    }
}
