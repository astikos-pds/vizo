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

    @DeleteMapping("/{municipalityId}/departments/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            Authentication authentication
    ) {
        this.municipalityService.deleteDepartment(municipalityId, departmentId, authentication);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{municipalityId}/departments/{departmentId}/assignments")
    public ResponseEntity<PageResponse<AssignmentDTO>> getAssignments(
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            Pageable pageable,
            Authentication authentication
    ) {
        Page<AssignmentDTO> response = this.municipalityService.getAssignments(
                municipalityId,
                departmentId,
                pageable,
                authentication
        );

        return ResponseEntity.ok(PageResponse.of(response));
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

    @PutMapping("/{municipalityId}/departments/{departmentId}/assignments/batch")
    public ResponseEntity<List<AssignmentDTO>> createOrUpdateAssignmentInBatch(
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @RequestBody BatchUpdateAssignmentRequestDTO body,
            Authentication authentication
    ) {
        List<AssignmentDTO> response = this.municipalityService.createOrUpdateAssignmentInBatch(
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
}
