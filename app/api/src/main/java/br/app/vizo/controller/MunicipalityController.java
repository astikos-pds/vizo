package br.app.vizo.controller;

import br.app.vizo.controller.filter.AffiliationRequestFilter;
import br.app.vizo.controller.request.BatchUpdateAssignmentRequestDTO;
import br.app.vizo.controller.request.UpdateAffiliationRequestDTO;
import br.app.vizo.controller.request.CreateDepartmentRequestDTO;
import br.app.vizo.controller.request.UpdateAssignmentRequestDTO;
import br.app.vizo.controller.response.*;
import br.app.vizo.service.MunicipalityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/municipalities")
public class MunicipalityController {

    private final MunicipalityService municipalityService;

    public MunicipalityController(MunicipalityService municipalityService) {
        this.municipalityService = municipalityService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MunicipalityDTO> getMunicipalityById(@PathVariable UUID id) {
        MunicipalityDTO response = this.municipalityService.getMunicipalityById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<MunicipalityDTO> getMunicipalityByEmailDomain(
            @RequestParam("domain") String emailDomain
    ) {
        MunicipalityDTO response = this.municipalityService.getMunicipalityByEmailDomain(emailDomain);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{municipalityId}/affiliations")
    public ResponseEntity<PageResponse<AffiliationRequestDTO>> getMunicipalityAffiliations(
            @PathVariable UUID municipalityId,
            @ModelAttribute AffiliationRequestFilter filter,
            Pageable pageable,
            Authentication authentication
    ) {
        Page<AffiliationRequestDTO> response = this.municipalityService
                .getMunicipalityAffiliations(municipalityId, filter, pageable, authentication);

        return ResponseEntity.ok(PageResponse.of(response));
    }

    @PatchMapping("/{municipalityId}/affiliations/{affiliationId}")
    public ResponseEntity<AffiliationRequestDTO> updateMunicipalityAffiliation(
            @PathVariable UUID municipalityId,
            @PathVariable String affiliationId,
            @RequestBody UpdateAffiliationRequestDTO body,
            Authentication authentication
    ) {
        AffiliationRequestDTO response = this.municipalityService
                .updateMunicipalityAffiliation(municipalityId, affiliationId, body, authentication);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{municipalityId}/departments/{departmentId}/problems")
    public ResponseEntity<PageResponse<ProblemDTO>> getAllDepartmentProblems(
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            Pageable pageable,
            Authentication authentication
    ) {
        Page<ProblemDTO> response = this.municipalityService.getDepartmentVisibleProblems(
                municipalityId,
                departmentId,
                pageable,
                authentication
        );

        return ResponseEntity.ok(PageResponse.of(response));
    }

    @GetMapping("/{municipalityId}/departments/{departmentId}/problems/{problemId}")
    public ResponseEntity<ProblemDTO> getDepartmentProblemById(
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @PathVariable UUID problemId,
            Authentication authentication
    ) {
        ProblemDTO response = this.municipalityService.getDepartmentProblemById(
                municipalityId,
                departmentId,
                problemId,
                authentication
        );

        return ResponseEntity.ok(response);
    }

}
