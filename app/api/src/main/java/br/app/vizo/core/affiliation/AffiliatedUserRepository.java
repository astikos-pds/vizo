package br.app.vizo.core.affiliation;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;

import java.util.Optional;
import java.util.UUID;

public interface AffiliatedUserRepository {

    AffiliatedUser save(AffiliatedUser user);

    Optional<AffiliatedUser> findById(UUID id);

    void deleteById(UUID id);

    PageDTO<AffiliatedUser> findAllByMunicipalityId(UUID id, PaginationDTO pagination);

    PageDTO<AffiliatedUser> findAllByMunicipalityIdAndStatus(UUID id, AffiliationStatus status, PaginationDTO pagination);

    Optional<AffiliatedUser> findByUserIdAndMunicipalityIdAndStatus(UUID userId, UUID municipalityId, AffiliationStatus status);

    Iterable<AffiliatedUser> findAllByUserId(UUID id);
}
