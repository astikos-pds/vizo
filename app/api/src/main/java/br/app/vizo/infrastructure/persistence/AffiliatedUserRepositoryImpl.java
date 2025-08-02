package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.AffiliatedUserMapper;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.affiliation.AffiliatedUserRepository;
import br.app.vizo.core.affiliation.AffiliationStatus;
import br.app.vizo.infrastructure.persistence.jpa.entity.AffiliatedUserEntity;
import br.app.vizo.infrastructure.persistence.jpa.repository.AffiliatedUserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AffiliatedUserRepositoryImpl implements AffiliatedUserRepository {

    private final AffiliatedUserJpaRepository jpaRepository;
    private final AffiliatedUserMapper mapper;

    public AffiliatedUserRepositoryImpl(AffiliatedUserJpaRepository jpaRepository, AffiliatedUserMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public AffiliatedUser save(AffiliatedUser user) {
        AffiliatedUserEntity entity = this.mapper.toEntity(user);
        AffiliatedUserEntity saved = this.jpaRepository.save(entity);
        return this.mapper.toModel(saved);
    }

    @Override
    public Optional<AffiliatedUser> findById(UUID id) {
        return this.jpaRepository.findById(id).map(this.mapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {
        this.jpaRepository.deleteById(id);
    }

    @Override
    public PageDTO<AffiliatedUser> findAllByMunicipalityId(UUID id, PaginationDTO pagination) {
        var page = this.jpaRepository.findAllByMunicipalityId(id, PaginationDTO.resolve(pagination))
                .map(this.mapper::toModel);
        return PageDTO.of(page);
    }

    @Override
    public PageDTO<AffiliatedUser> findAllByMunicipalityIdAndStatus(UUID id, AffiliationStatus status, PaginationDTO pagination) {
        var page = this.jpaRepository.findAllByMunicipalityIdAndStatus(id, status, PaginationDTO.resolve(pagination))
                .map(this.mapper::toModel);
        return PageDTO.of(page);
    }

    @Override
    public Optional<AffiliatedUser> findByUserIdAndMunicipalityIdAndStatus(UUID userId, UUID municipalityId, AffiliationStatus status) {
        return this.jpaRepository.findByUserIdAndMunicipalityIdAndStatus(userId, municipalityId, status)
                .map(this.mapper::toModel);
    }

    @Override
    public Iterable<AffiliatedUser> findAllByUserId(UUID id) {
        return this.jpaRepository.findAllByUserId(id).stream().map(this.mapper::toModel).toList();
    }
}
