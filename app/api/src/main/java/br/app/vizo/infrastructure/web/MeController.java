package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.*;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.usecase.me.*;
import br.app.vizo.application.usecase.me.filter.GetMyAffiliationsParams;
import br.app.vizo.application.usecase.me.filter.GetMyNotificationsParams;
import br.app.vizo.application.usecase.me.filter.ReportFilter;
import br.app.vizo.config.security.UserDetailsImpl;
import br.app.vizo.core.affiliation.AffiliationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final GetMeUseCase getMeUseCase;
    private final GetMyNotificationsUseCase getMyNotificationsUseCase;
    private final GetMyPointsOfInterestUseCase getMyPointsOfInterestUseCase;
    private final GetMyAffiliationsUseCase getMyAffiliationsUseCase;
    private final GetMyAssignmentsInMunicipalityUseCase getMyAssignmentsInMunicipalityUseCase;
    private final GetMyReportsUseCase getMyReportsUseCase;
    private final DisaffiliateFromMunicipalityUseCase disaffiliateFromMunicipalityUseCase;
    private final LeaveDepartmentUseCase leaveDepartmentUseCase;

    @GetMapping
    public ResponseEntity<UserDTO> getMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserDTO response = this.getMeUseCase.execute(userDetails.getUser());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<NotificationDTO<?>>> getMyNotification(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false) Boolean read
    ) {
        List<NotificationDTO<?>> response = this.getMyNotificationsUseCase
                .execute(userDetails.getUser(), new GetMyNotificationsParams(read));

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = {"/points-of-interest", "/poi"})
    public ResponseEntity<PageDTO<PointOfInterestDTO>> getMyPointsOfInterest(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            PaginationDTO pagination
    ) {
        return ResponseEntity.ok(this.getMyPointsOfInterestUseCase.execute(userDetails.getUser(), pagination));
    }

    @GetMapping("/affiliations")
    public ResponseEntity<PageDTO<AffiliatedUserDTO>> getMyAffiliations(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            PaginationDTO pagination,
            @RequestParam(required = false) AffiliationStatus status
    ) {
        PageDTO<AffiliatedUserDTO> response = this.getMyAffiliationsUseCase
                .execute(userDetails.getUser(), pagination, new GetMyAffiliationsParams(status));

        return ResponseEntity.ok(response);
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
