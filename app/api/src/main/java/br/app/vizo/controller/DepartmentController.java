package br.app.vizo.controller;

import br.app.vizo.controller.request.CreateDepartmentRequestDTO;
import br.app.vizo.controller.response.DepartmentDTO;
import br.app.vizo.controller.response.PageResponse;
import br.app.vizo.service.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/municipalities/{municipalityId}/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<DepartmentDTO>> getDepartments(
            @PathVariable UUID municipalityId,
            Pageable pageable,
            Authentication authentication
    ) {
        Page<DepartmentDTO> response = this.departmentService.getDepartments(
                municipalityId, pageable, authentication
        );

        return ResponseEntity.ok(PageResponse.of(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartment(
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            Authentication authentication
    ) {
        DepartmentDTO response = this.departmentService.getDepartment(
                municipalityId, departmentId, authentication
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(
            @PathVariable UUID municipalityId,
            @RequestBody CreateDepartmentRequestDTO body,
            Authentication authentication
    ) {
        DepartmentDTO response = this.departmentService.createDepartment(
                municipalityId, body, authentication
        );

        var uri = UriComponentsBuilder.fromPath("/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            Authentication authentication
    ) {
        this.departmentService.deleteDepartment(municipalityId, departmentId, authentication);

        return ResponseEntity.ok().build();
    }
}
