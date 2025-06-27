package br.app.vizo.controller;

import br.app.vizo.controller.response.PageResponse;
import br.app.vizo.controller.response.ProblemDTO;
import br.app.vizo.controller.response.ReportDTO;
import br.app.vizo.service.ProblemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProblemDTO>> getProblems(
            @RequestParam(required = false, defaultValue = "false") Boolean validated,
            Pageable pageable
    ) {
        if (validated) {
            return ResponseEntity.ok(
                    PageResponse.of(this.problemService.getValidatedProblems(pageable))
            );
        }
        return ResponseEntity.ok(
                PageResponse.of(this.problemService.getProblems(pageable))
        );
    }

    @GetMapping("/{id}/reports")
    public ResponseEntity<PageResponse<ReportDTO>> getProblemReports(@PathVariable String id, Pageable pageable) {
        Page<ReportDTO> response = this.problemService.getProblemReports(id, pageable);

        return ResponseEntity.ok(PageResponse.of(response));
    }
}
