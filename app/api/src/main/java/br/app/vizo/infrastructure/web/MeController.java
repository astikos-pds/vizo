package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.AffiliatedUserDTO;
import br.app.vizo.application.dto.AssignedUserDTO;
import br.app.vizo.application.dto.ReportDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.usecase.me.*;
import br.app.vizo.application.usecase.me.filter.ReportFilter;
import br.app.vizo.config.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final GetMyAffiliationsUseCase getMyAffiliationsUseCase;
    private final GetMyAssignmentsInMunicipalityUseCase getMyAssignmentsInMunicipalityUseCase;
    private final GetMyReportsUseCase getMyReportsUseCase;
    private final DisaffiliateFromMunicipalityUseCase disaffiliateFromMunicipalityUseCase;
    private final LeaveDepartmentUseCase leaveDepartmentUseCase;

    @GetMapping("/affiliations")
    public ResponseEntity<PageDTO<AffiliatedUserDTO>> getMyAffiliations(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            PaginationDTO pagination
    ) {
        return ResponseEntity.ok(this.getMyAffiliationsUseCase.execute(userDetails.getUser(), pagination));
    }

    @GetMapping("/municipalities/{municipalityId}/assignments")
    public ResponseEntity<PageDTO<AssignedUserDTO>> getMyAssignmentsInMunicipality(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            PaginationDTO pagination
    ) {
        return ResponseEntity.ok(
                this.getMyAssignmentsInMunicipalityUseCase.execute(userDetails.getUser(), municipalityId, pagination)
        );
    }

    @GetMapping("/reports")
    public ResponseEntity<PageDTO<ReportDTO>> getMyReports(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            ReportFilter reportFilter,
            PaginationDTO pagination
    ) {
        return ResponseEntity.ok(this.getMyReportsUseCase.execute(userDetails.getUser(), reportFilter, pagination));
    }

    @DeleteMapping("/municipalities/{municipalityId}/affiliations")
    public ResponseEntity<Void> disaffiliateFromMunicipality(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId
    ) {
        this.disaffiliateFromMunicipalityUseCase.execute(userDetails.getUser(), municipalityId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/municipalities/{municipalityId}/department/{departmentId}/assignments")
    public ResponseEntity<Void> leaveDepartment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId
    ) {
        this.leaveDepartmentUseCase.execute(userDetails.getUser(), municipalityId, departmentId);

        return ResponseEntity.ok().build();
    }
}
