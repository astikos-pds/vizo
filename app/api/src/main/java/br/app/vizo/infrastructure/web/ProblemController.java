package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.ProblemDTO;
import br.app.vizo.application.dto.ReportDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.usecase.problem.GetProblemTypesUseCase;
import br.app.vizo.application.usecase.problem.GetProblemsUseCase;
import br.app.vizo.application.usecase.problem.GetReportsForProblemUseCase;
import br.app.vizo.core.problem.ProblemType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final GetProblemsUseCase getProblemsUseCase;
    private final GetProblemTypesUseCase getProblemTypesUseCase;
    private final GetReportsForProblemUseCase getReportsForProblemUseCase;

    @GetMapping
    public ResponseEntity<List<ProblemDTO>> getProblems() {
        return ResponseEntity.ok(this.getProblemsUseCase.execute());
    }

    @GetMapping("/types")
    public ResponseEntity<Set<ProblemType>> getProblemTypes() {
        return ResponseEntity.ok(this.getProblemTypesUseCase.execute());
    }

    @GetMapping("/{id}/reports")
    public ResponseEntity<PageDTO<ReportDTO>> getReportsForProblem(
            @PathVariable UUID id,
            @ModelAttribute PaginationDTO pagination
    ) {
        return ResponseEntity.ok(this.getReportsForProblemUseCase.execute(id, pagination));
    }
}
