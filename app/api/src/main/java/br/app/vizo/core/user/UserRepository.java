package br.app.vizo.core.user;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByDocument(String document);

    boolean existsByDocumentOrEmail(String document, String email);
}
