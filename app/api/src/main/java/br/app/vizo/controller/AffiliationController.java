package br.app.vizo.controller;

import br.app.vizo.controller.filter.AffiliationFilter;
import br.app.vizo.controller.request.CreateAffiliationRequestDTO;
import br.app.vizo.controller.request.UpdateAffiliationDTO;
import br.app.vizo.dto.AffiliationDTO;
import br.app.vizo.dto.PageResponse;
import br.app.vizo.service.AffiliationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/municipalities/{municipalityId}/affiliations")
public class AffiliationController {

    private final AffiliationService affiliationService;

    public AffiliationController(AffiliationService affiliationService) {
        this.affiliationService = affiliationService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<AffiliationDTO>> getAffiliations(
            @PathVariable UUID municipalityId,
            @ModelAttribute AffiliationFilter filter,
            Pageable pageable,
            Authentication authentication
    ) {
        Page<AffiliationDTO> response = this.affiliationService
                .getAffiliations(municipalityId, filter, pageable, authentication);

        return ResponseEntity.ok(PageResponse.of(response));
    }

    @PostMapping
    public ResponseEntity<AffiliationDTO> createAffiliation(
            @PathVariable UUID municipalityId,
            @RequestBody CreateAffiliationRequestDTO body,
            Authentication authentication
    ) {
        AffiliationDTO response = this.affiliationService.createAffiliation(municipalityId, body, authentication);

        var uri = UriComponentsBuilder.fromPath("/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AffiliationDTO> updateAffiliation(
            @PathVariable UUID municipalityId,
            @PathVariable(name = "id") UUID affiliationId,
            @RequestBody UpdateAffiliationDTO body,
            Authentication authentication
    ) {
        AffiliationDTO response = this.affiliationService
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
