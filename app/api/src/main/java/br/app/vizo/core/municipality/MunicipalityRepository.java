package br.app.vizo.core.municipality;

import java.util.Optional;
import java.util.UUID;

public interface MunicipalityRepository {

    Optional<Municipality> findById(UUID id);

    Optional<Municipality> findByEmailDomain(String emailDomain);

    boolean existsById(UUID id);
}
