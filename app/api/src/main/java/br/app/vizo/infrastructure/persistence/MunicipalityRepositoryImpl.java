package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.mapper.MunicipalityMapper;
import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.municipality.MunicipalityRepository;
import br.app.vizo.infrastructure.persistence.jpa.repository.MunicipalityJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class MunicipalityRepositoryImpl implements MunicipalityRepository {

    private final MunicipalityJpaRepository jpaRepository;
    private final MunicipalityMapper mapper;

    public MunicipalityRepositoryImpl(MunicipalityJpaRepository jpaRepository, MunicipalityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Municipality> findById(UUID id) {
        return this.jpaRepository.findById(id).map(this.mapper::toModel);
    }

    @Override
    public Optional<Municipality> findByEmailDomain(String emailDomain) {
        return this.jpaRepository.findByEmailDomain(emailDomain).map(this.mapper::toModel);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.jpaRepository.existsById(id);
    }
}
