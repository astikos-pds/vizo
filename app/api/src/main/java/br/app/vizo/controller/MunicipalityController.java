package br.app.vizo.controller;

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

import java.util.UUID;

@RestController
@RequestMapping("/municipalities")
public class MunicipalityController {

    private final MunicipalityService municipalityService;

    public MunicipalityController(MunicipalityService municipalityService) {
        this.municipalityService = municipalityService;
    }

    @GetMapping("/{municipalityId}/officials")
    public ResponseEntity<PageResponse<OfficialDTO>> getOfficials(
            @PathVariable UUID municipalityId,
            Pageable pageable,
            Authentication authentication
    ) {
        Page<OfficialDTO> response = this.municipalityService.getOfficials(municipalityId, pageable, authentication);

        return ResponseEntity.ok(PageResponse.of(response));
    }

    @GetMapping("/{municipalityId}/departments")
    public ResponseEntity<PageResponse<DepartmentDTO>> getDepartments(
            @PathVariable UUID municipalityId,
            Pageable pageable,
            Authentication authentication
    ) {
        Page<DepartmentDTO> response = this.municipalityService
                .getDepartments(municipalityId, pageable, authentication);

        return ResponseEntity.ok(PageResponse.of(response));
    }

    @PostMapping("/{municipalityId}/departments")
    public ResponseEntity<DepartmentDTO> createDepartment(
            @PathVariable UUID municipalityId,
            @RequestBody CreateDepartmentRequestDTO body,
            Authentication authentication
    ) {
        DepartmentDTO response = this.municipalityService.createMunicipalityDepartment(
                municipalityId,
                body,
                authentication
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{municipalityId}/departments/{departmentId}/assignments")
    public ResponseEntity<AssignmentDTO> createOrUpdateAssignment(
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @RequestBody UpdateAssignmentRequestDTO body,
            Authentication authentication
    ) {
        AssignmentDTO response = this.municipalityService.createOrUpdateAssignment(
                municipalityId,
                departmentId,
                body,
                authentication
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{municipalityId}/departments/{departmentId}/assignments/{assignmentId}")
    public ResponseEntity<AssignmentDTO> getAssignment(
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @PathVariable UUID assignmentId,
            Authentication authentication
    ) {
        AssignmentDTO response = this.municipalityService.getAssignment(
                municipalityId,
                departmentId,
                assignmentId,
                authentication
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{municipalityId}/affiliations")
    public ResponseEntity<PageResponse<AffiliationRequestDTO>> getMunicipalityAffiliations(
            @PathVariable UUID municipalityId,
            Pageable pageable,
            Authentication authentication
    ) {
        Page<AffiliationRequestDTO> response = this.municipalityService
                .getMunicipalityAffiliations(municipalityId, pageable, authentication);

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
}
