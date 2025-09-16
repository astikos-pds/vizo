package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.ReportDTO;
import br.app.vizo.application.usecase.report.DeleteReportUseCase;
import br.app.vizo.application.usecase.report.ReportProblemUseCase;
import br.app.vizo.application.usecase.report.UpdateReportUseCase;
import br.app.vizo.application.usecase.report.request.ReportProblemRequestDTO;
import br.app.vizo.config.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportProblemUseCase reportProblemUseCase;
    private final UpdateReportUseCase updateReportUseCase;
    private final DeleteReportUseCase deleteReportUseCase;

    @PostMapping
    public ResponseEntity<ReportDTO> reportProblem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ReportProblemRequestDTO body
    ) {
        ReportDTO response = this.reportProblemUseCase.execute(userDetails.getUser(), body);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportDTO> updateReport(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID id,
            @RequestBody ReportProblemRequestDTO body
    ) {
        ReportDTO response = this.updateReportUseCase.execute(userDetails.getUser(), id, body);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID id
    ) {
        this.deleteReportUseCase.execute(userDetails.getUser(), id);

        return ResponseEntity.ok().build();
    }
}
