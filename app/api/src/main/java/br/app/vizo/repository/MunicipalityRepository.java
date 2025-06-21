package br.app.vizo.repository;

import br.app.vizo.domain.municipality.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MunicipalityRepository extends JpaRepository<Municipality, UUID> {

    Optional<Municipality> findByEmailDomain(String emailDomain);
}
