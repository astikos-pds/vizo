package br.app.vizo.controller;

import br.app.vizo.controller.request.UpdateAffiliationRequestDTO;
import br.app.vizo.controller.response.AffiliationRequestDTO;
import br.app.vizo.service.MunicipalityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/municipalities")
public class MunicipalityController {

    private final MunicipalityService municipalityService;

    public MunicipalityController(MunicipalityService municipalityService) {
        this.municipalityService = municipalityService;
    }

    @PatchMapping("/{municipalityId}/affiliations/{affiliationId}")
    public ResponseEntity<AffiliationRequestDTO> updateMunicipalityAffiliation(
            @PathVariable String municipalityId,
            @PathVariable String affiliationId,
            @RequestBody UpdateAffiliationRequestDTO body,
            Authentication authentication
    ) {
        AffiliationRequestDTO response = this.municipalityService
                .updateMunicipalityAffiliation(municipalityId, affiliationId, body, authentication);

        return ResponseEntity.ok(response);
    }
}
