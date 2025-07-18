package br.app.vizo.controller;

import br.app.vizo.controller.request.BatchUpdateAssignmentRequestDTO;
import br.app.vizo.controller.request.UpdateAssignmentRequestDTO;
import br.app.vizo.controller.response.AssignmentDTO;
import br.app.vizo.controller.response.PageResponse;
import br.app.vizo.service.AssignmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/municipalities/{municipalityId}/departments/{departmentId}/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<AssignmentDTO>> getAssignments(
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            Pageable pageable,
            Authentication authentication
    ) {
        Page<AssignmentDTO> response = this.assignmentService.getAssignments(
                municipalityId, departmentId, pageable, authentication
        );

        return ResponseEntity.ok(PageResponse.of(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDTO> getAssignment(
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @PathVariable(name = "id") UUID assignmentId,
            Authentication authentication
    ) {
        AssignmentDTO response = this.assignmentService.getAssignment(
                municipalityId, departmentId, assignmentId, authentication
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<AssignmentDTO> createOrUpdateAssignment(
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @RequestBody UpdateAssignmentRequestDTO body,
            Authentication authentication
    ) {
        AssignmentDTO response = this.assignmentService.createOrUpdateAssignment(
                municipalityId, departmentId, body, authentication
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/batch")
    public ResponseEntity<List<AssignmentDTO>> createOrUpdateAssignmentInBatch(
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @RequestBody BatchUpdateAssignmentRequestDTO body,
            Authentication authentication
    ) {
        List<AssignmentDTO> response = this.assignmentService.createOrUpdateAssignmentInBatch(
                municipalityId, departmentId, body, authentication
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @PathVariable(name = "id") UUID assignmentId,
            Authentication authentication
    ) {
        this.assignmentService.deleteAssignment(municipalityId, departmentId, assignmentId, authentication);

        return ResponseEntity.ok().build();
    }
}
