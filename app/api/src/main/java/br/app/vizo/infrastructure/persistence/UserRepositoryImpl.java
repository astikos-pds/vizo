package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.mapper.UserMapper;
import br.app.vizo.core.user.User;
import br.app.vizo.core.user.UserRepository;
import br.app.vizo.infrastructure.persistence.jpa.entity.UserEntity;
import br.app.vizo.infrastructure.persistence.jpa.repository.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    public UserRepositoryImpl(UserJpaRepository jpaRepository, UserMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = this.mapper.toEntity(user);
        UserEntity saved = this.jpaRepository.save(userEntity);
        return this.mapper.toModel(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return this.jpaRepository.findById(id).map(this.mapper::toModel);
    }

    @Override
    public Optional<User> findByDocument(String document) {
        String numericOnlyDocument = this.clearSpecialCharactersFromDocument(document);
        return this.jpaRepository.findByDocument(numericOnlyDocument)
                .map(this.mapper::toModel);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.jpaRepository.findByEmail(email)
                .map(this.mapper::toModel);
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByDocumentAndEmail(String document, String email) {
        String cleanedDocument = this.clearSpecialCharactersFromDocument(document);
        return this.jpaRepository.existsByDocumentAndEmail(cleanedDocument, email);
    }

    @Override
    public boolean existsByDocumentOrEmail(String document, String email) {
        return this.jpaRepository.existsByDocumentOrEmail(document, email);
    }

    private String clearSpecialCharactersFromDocument(String document) {
        return document.replaceAll("\\D", "");
    }
}
