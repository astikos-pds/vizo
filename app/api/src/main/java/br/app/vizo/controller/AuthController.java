package br.app.vizo.controller;

import br.app.vizo.domain.user.dto.CitizenDTO;
import br.app.vizo.domain.user.dto.RegisterCitizenRequestDTO;
import br.app.vizo.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/citizen")
    public ResponseEntity<CitizenDTO> registerCitizen(@RequestBody RegisterCitizenRequestDTO request) {
        CitizenDTO response = this.authService.registerCitizen(
                request.document(),
                request.email(),
                request.password(),
                request.name()
        );

        return ResponseEntity.ok(response);
    }
}
