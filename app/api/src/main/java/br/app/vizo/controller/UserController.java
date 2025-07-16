package br.app.vizo.controller;

import br.app.vizo.controller.response.AffiliationRequestDTO;
import br.app.vizo.controller.response.AssignmentDTO;
import br.app.vizo.controller.response.profile.ProfileDTO;
import br.app.vizo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/me")
    public ResponseEntity<ProfileDTO> getProfile(Authentication authentication) {
        ProfileDTO response = this.userService.getLoggedInUser(authentication);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/officials/me/affiliations")
    public ResponseEntity<List<AffiliationRequestDTO>> getAffiliations(
            Authentication authentication
    ) {
        List<AffiliationRequestDTO> response = this.userService.getAffiliations(authentication);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/officials/me/municipalities/{id}/assignments")
    public ResponseEntity<List<AssignmentDTO>> getAssignments(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        List<AssignmentDTO> response = this.userService.getAssignments(id, authentication);

        return ResponseEntity.ok(response);
    }
}
