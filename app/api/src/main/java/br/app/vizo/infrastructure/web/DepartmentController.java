package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.DepartmentDTO;
import br.app.vizo.application.dto.ProblemDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.usecase.department.*;
import br.app.vizo.application.usecase.department.param.GetProblemsInScopeStatisticsParams;
import br.app.vizo.application.usecase.department.request.ChangeProblemStatusRequestDTO;
import br.app.vizo.application.usecase.department.request.MutateDepartmentRequestDTO;
import br.app.vizo.config.security.UserDetailsImpl;
import br.app.vizo.core.problem.ProblemCountByStatus;
import br.app.vizo.core.problem.ProblemStatistics;
import br.app.vizo.core.problem.ProblemStatus;
import br.app.vizo.core.problem.ProblemType;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
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
    private final ChangeProblemInScopeStatusUseCase changeProblemInScopeStatusUseCase;
    private final GetProblemsInScopeStatisticsUseCase getProblemsInScopeStatisticsUseCase;
    private final CountProblemsInScopeByStatusUseCase countProblemsInScopeByStatusUseCase;

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
        PageDTO<ProblemDTO> response = this.getProblemsInScopeUseCase
                .execute(userDetails.getUser(), municipalityId, departmentId, pagination);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{departmentId}/problems/{id}")
    public ResponseEntity<ProblemDTO> getProblemInScope(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @PathVariable(name = "id") UUID problemId
    ) {
        ProblemDTO response = this.getProblemInScopeUseCase
                .execute(userDetails.getUser(), municipalityId, departmentId, problemId);

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{departmentId}/problems/{id}")
    public ResponseEntity<ProblemDTO> changeProblemInScopeStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @PathVariable(name = "id") UUID problemId,
            @RequestBody ChangeProblemStatusRequestDTO body
    ) {
        ProblemDTO response = this.changeProblemInScopeStatusUseCase
                .execute(userDetails.getUser(), municipalityId, departmentId, problemId, body);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{departmentId}/problems/statistics")
    public ResponseEntity<List<ProblemStatistics>> getProblemsInScopeStatistics(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(required = false) Set<ProblemStatus> statuses,
            @RequestParam(required = false) Set<ProblemType> types
    ) {
        List<ProblemStatistics> response = this.getProblemsInScopeStatisticsUseCase.execute(
                userDetails.getUser(),
                municipalityId,
                departmentId,
                new GetProblemsInScopeStatisticsParams(start, end, statuses, types)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{departmentId}/problems/count")
    public ResponseEntity<List<ProblemCountByStatus>> countProblemsInScopeByStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId
    ) {
        List<ProblemCountByStatus> response = this.countProblemsInScopeByStatusUseCase.execute(
                userDetails.getUser(),
                municipalityId,
                departmentId
        );

        return ResponseEntity.ok(response);
    }

}
