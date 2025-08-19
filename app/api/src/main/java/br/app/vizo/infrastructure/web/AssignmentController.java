package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.AssignedUserDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.usecase.assignment.*;
import br.app.vizo.application.usecase.assignment.request.AssignUserToDepartmentRequestDTO;
import br.app.vizo.application.usecase.assignment.request.AssignUsersToDepartmentRequestDTO;
import br.app.vizo.config.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/municipalities/{municipalityId}/departments/{departmentId}/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final GetUsersAssignedToDepartmentUseCase getUsersAssignedToDepartmentUseCase;
    private final GetUserAssignedToDepartmentUseCase getUserAssignedToDepartmentUseCase;
    private final AssignUserToDepartmentUseCase assignUserToDepartmentUseCase;
    private final AssignUsersToDepartmentUseCase assignUsersToDepartmentUseCase;
    private final RemoveAssigneeFromDepartmentUseCase removeAssigneeFromDepartmentUseCase;

    @GetMapping
    public ResponseEntity<PageDTO<AssignedUserDTO>> getUsersAssignedToDepartment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            PaginationDTO pagination
    ) {
        PageDTO<AssignedUserDTO> response = this.getUsersAssignedToDepartmentUseCase.execute(
                userDetails.getUser(),
                municipalityId,
                departmentId,
                pagination
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignedUserDTO> getUserAssignedToDepartment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @PathVariable(name = "id") UUID assignmentId
    ) {
        AssignedUserDTO response = this.getUserAssignedToDepartmentUseCase.execute(
                userDetails.getUser(),
                municipalityId,
                departmentId,
                assignmentId
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<AssignedUserDTO> assignUserToDepartment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @RequestBody AssignUserToDepartmentRequestDTO body
    ) {
        AssignedUserDTO response = this.assignUserToDepartmentUseCase.execute(
                userDetails.getUser(),
                municipalityId,
                departmentId,
                body
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/batch")
    public ResponseEntity<List<AssignedUserDTO>> assignUsersToDepartment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @RequestBody AssignUsersToDepartmentRequestDTO body
    ) {
        List<AssignedUserDTO> response = this.assignUsersToDepartmentUseCase.execute(
                userDetails.getUser(),
                municipalityId,
                departmentId,
                body
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAssigneeFromDepartment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable UUID departmentId,
            @PathVariable(name = "id") UUID assignmentId
    ) {
        this.removeAssigneeFromDepartmentUseCase.execute(
                userDetails.getUser(),
                municipalityId,
                departmentId,
                assignmentId
        );

        return ResponseEntity.ok().build();
    }

}
