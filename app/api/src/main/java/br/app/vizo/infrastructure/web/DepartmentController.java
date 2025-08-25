package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.DepartmentDTO;
import br.app.vizo.application.dto.ProblemDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.usecase.department.*;
import br.app.vizo.application.usecase.department.request.MutateDepartmentRequestDTO;
import br.app.vizo.config.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/municipalities/{municipalityId}/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final GetDepartmentByIdUseCase getDepartmentByIdUseCase;
    private final CreateDepartmentUseCase createDepartmentUseCase;
    private final UpdateDepartmentUseCase updateDepartmentUseCase;
    private final DeleteDepartmentUseCase deleteDepartmentUseCase;
    private final GetProblemsInScopeUseCase getProblemsInScopeUseCase;
    private final GetProblemInScopeUseCase getProblemInScopeUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable(name = "id") UUID departmentId
    ) {
        return ResponseEntity.ok(
                this.getDepartmentByIdUseCase.execute(userDetails.getUser(), municipalityId, departmentId)
        );
    }

    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @RequestBody MutateDepartmentRequestDTO body
    ) {
        DepartmentDTO response = this.createDepartmentUseCase.execute(userDetails.getUser(), municipalityId, body);

        URI uri = UriComponentsBuilder.fromPath("/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable(name = "id") UUID departmentId,
            @RequestBody MutateDepartmentRequestDTO body
    ) {
        return ResponseEntity.ok(
                this.updateDepartmentUseCase.execute(userDetails.getUser(), municipalityId, departmentId, body)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable(name = "id") UUID departmentId
    ) {
        this.deleteDepartmentUseCase.execute(userDetails.getUser(), municipalityId, departmentId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/problems")
    public ResponseEntity<PageDTO<ProblemDTO>> getProblemsInScope(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable(name = "id") UUID departmentId,
            PaginationDTO pagination
    ) {
        return ResponseEntity.ok(
                this.getProblemsInScopeUseCase.execute(userDetails.getUser(), municipalityId, departmentId, pagination)
        );
    }

    @GetMapping("/{id}/problems/{problemId}")
    public ResponseEntity<ProblemDTO> getProblemInScope(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable(name = "id") UUID departmentId,
            @PathVariable UUID problemId
    ) {
        return ResponseEntity.ok(
                this.getProblemInScopeUseCase.execute(userDetails.getUser(), municipalityId, departmentId, problemId)
        );
    }
}
