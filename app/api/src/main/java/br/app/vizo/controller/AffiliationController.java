package br.app.vizo.controller;

import br.app.vizo.controller.request.UpdateAffiliationRequestDTO;
import br.app.vizo.controller.response.AffiliationRequestDTO;
import br.app.vizo.controller.response.OfficialDTO;
import br.app.vizo.service.AffiliationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/affiliations")
public class AffiliationController {

    private final AffiliationService affiliationService;

    public AffiliationController(AffiliationService affiliationService) {
        this.affiliationService = affiliationService;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AffiliationRequestDTO> updateAffiliation(
            @PathVariable String id,
            @RequestBody UpdateAffiliationRequestDTO body,
            Authentication authentication
    ) {
        AffiliationRequestDTO response = this.affiliationService.updateAffiliation(id, body, authentication);

        return ResponseEntity.ok(response);
    }
}
