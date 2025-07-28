package br.app.vizo.repository;

import br.app.vizo.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OldUserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByDocument(String document);

    boolean existsByDocumentOrEmail(String document, String email);
}
