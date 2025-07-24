package br.app.vizo.core.user;

public interface PasswordHasher {

    String hash(String raw);
    boolean matches(String raw, String hashed);
}
