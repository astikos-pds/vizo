package br.app.vizo.controller;

import br.app.vizo.controller.request.CreateReportRequestDTO;
import br.app.vizo.controller.response.ReportDTO;
import br.app.vizo.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<ReportDTO> createReport(
            @RequestBody CreateReportRequestDTO body,
            Authentication authentication
    ) {
        ReportDTO response = this.reportService.createReport(body, authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
