package br.app.vizo.core.user;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByDocument(String document);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByDocumentAndEmail(String document, String email);

    boolean existsByDocumentOrEmail(String document, String email);
}
