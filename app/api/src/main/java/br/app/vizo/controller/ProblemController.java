package br.app.vizo.controller;

import br.app.vizo.controller.request.UpdateProblemRequestDTO;
import br.app.vizo.controller.response.PageResponse;
import br.app.vizo.controller.response.ProblemDTO;
import br.app.vizo.controller.response.ReportDTO;
import br.app.vizo.service.ProblemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping
    public ResponseEntity<List<ProblemDTO>> getProblems(
            @RequestParam(required = false, defaultValue = "false") Boolean validated
    ) {
        if (validated) {
            return ResponseEntity.ok(
                    this.problemService.getValidatedProblems()
            );
        }
        return ResponseEntity.ok(
                this.problemService.getProblems()
        );
    }

    @GetMapping("/{id}/reports")
    public ResponseEntity<PageResponse<ReportDTO>> getProblemReports(@PathVariable UUID id, Pageable pageable) {
        Page<ReportDTO> response = this.problemService.getProblemReports(id, pageable);

        return ResponseEntity.ok(PageResponse.of(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProblemDTO> updateProblem(
            @PathVariable UUID id,
            @RequestBody UpdateProblemRequestDTO body,
            Authentication authentication
    ) {
        ProblemDTO response = this.problemService.updateProblem(id, body, authentication);

        return ResponseEntity.ok(response);
    }
}
