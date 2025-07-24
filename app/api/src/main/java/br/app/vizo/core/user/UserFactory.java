package br.app.vizo.core.user;

public interface UserFactory {

    User create(String name, String document, String email, String password);
}
