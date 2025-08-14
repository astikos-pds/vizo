package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.ReportDTO;
import br.app.vizo.application.usecase.report.ReportProblemUseCase;
import br.app.vizo.application.usecase.report.request.ReportProblemRequestDTO;
import br.app.vizo.config.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportProblemUseCase reportProblemUseCase;

    public ReportController(ReportProblemUseCase reportProblemUseCase) {
        this.reportProblemUseCase = reportProblemUseCase;
    }

    @PostMapping
    public ResponseEntity<ReportDTO> reportProblem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ReportProblemRequestDTO body
    ) {
        ReportDTO response = this.reportProblemUseCase.execute(userDetails.getUser(), body);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
