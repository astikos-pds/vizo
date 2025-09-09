package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.AffiliatedUserDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.usecase.affiliation.ChangeAffiliationStatusUseCase;
import br.app.vizo.application.usecase.affiliation.GetUsersAffiliatedToMunicipalityUseCase;
import br.app.vizo.application.usecase.affiliation.RemoveAffiliateFromMunicipalityUseCase;
import br.app.vizo.application.usecase.affiliation.RequestAffiliationToMunicipalityUseCase;
import br.app.vizo.application.usecase.affiliation.params.AffiliationStatusParam;
import br.app.vizo.application.usecase.affiliation.request.AffiliateToMunicipalityRequestDTO;
import br.app.vizo.application.usecase.affiliation.request.ChangeAffiliationStatusRequestDTO;
import br.app.vizo.config.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/municipalities/{municipalityId}/affiliations")
@RequiredArgsConstructor
public class AffiliationController {

    private final GetUsersAffiliatedToMunicipalityUseCase getUsersAffiliatedToMunicipalityUseCase;
    private final RequestAffiliationToMunicipalityUseCase requestAffiliationToMunicipalityUseCase;
    private final ChangeAffiliationStatusUseCase changeAffiliationStatusUseCase;
    private final RemoveAffiliateFromMunicipalityUseCase removeAffiliateFromMunicipalityUseCase;

    @GetMapping
    public ResponseEntity<PageDTO<AffiliatedUserDTO>> getUsersAffiliatedToMunicipality(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            PaginationDTO pagination,
            AffiliationStatusParam filter
    ) {
        return ResponseEntity.ok(
                this.getUsersAffiliatedToMunicipalityUseCase.execute(
                        userDetails.getUser(),
                        municipalityId,
                        pagination,
                        filter
                )
        );
    }

    @PostMapping
    public ResponseEntity<AffiliatedUserDTO> requestAffiliationToMunicipality(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @RequestBody AffiliateToMunicipalityRequestDTO body
    ) {
        AffiliatedUserDTO response = this.requestAffiliationToMunicipalityUseCase
                .execute(userDetails.getUser(), municipalityId, body);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AffiliatedUserDTO> changeAffiliationStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable(name = "id") UUID affiliationId,
            @RequestBody ChangeAffiliationStatusRequestDTO body
    ) {
        return ResponseEntity.ok(
                this.changeAffiliationStatusUseCase.execute(userDetails.getUser(), municipalityId, affiliationId, body)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAffiliateFromMunicipality(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable(name = "id") UUID affiliationId
    ) {
        this.removeAffiliateFromMunicipalityUseCase.execute(userDetails.getUser(), municipalityId, affiliationId);

        return ResponseEntity.ok().build();
    }
}
