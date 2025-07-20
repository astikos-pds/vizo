package br.app.vizo.controller;

import br.app.vizo.controller.filter.AffiliationRequestFilter;
import br.app.vizo.controller.request.UpdateAffiliationRequestDTO;
import br.app.vizo.controller.response.AffiliationRequestDTO;
import br.app.vizo.controller.response.PageResponse;
import br.app.vizo.service.AffiliationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/municipalities/{municipalityId}/affiliations")
public class AffiliationController {

    private final AffiliationService affiliationService;

    public AffiliationController(AffiliationService affiliationService) {
        this.affiliationService = affiliationService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<AffiliationRequestDTO>> getAffiliations(
            @PathVariable UUID municipalityId,
            @ModelAttribute AffiliationRequestFilter filter,
            Pageable pageable,
            Authentication authentication
    ) {
        Page<AffiliationRequestDTO> response = this.affiliationService
                .getAffiliations(municipalityId, filter, pageable, authentication);

        return ResponseEntity.ok(PageResponse.of(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AffiliationRequestDTO> updateAffiliation(
            @PathVariable UUID municipalityId,
            @PathVariable(name = "id") UUID affiliationId,
            @RequestBody UpdateAffiliationRequestDTO body,
            Authentication authentication
    ) {
        AffiliationRequestDTO response = this.affiliationService
                .updateAffiliation(municipalityId, affiliationId, body, authentication);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAffiliation(
            @PathVariable UUID municipalityId,
            @PathVariable(name = "id") UUID affiliationId,
            Authentication authentication
    ) {
        this.affiliationService.deleteAffiliation(municipalityId, affiliationId, authentication);

        return ResponseEntity.ok().build();
    }
}
