package br.app.vizo.infrastructure.persistence;

import br.app.vizo.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByDocument(String document);

    boolean existsByDocumentOrEmail(String document, String email);
}
