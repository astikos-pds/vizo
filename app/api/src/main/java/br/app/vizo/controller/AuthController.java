package br.app.vizo.controller;

import br.app.vizo.controller.request.LoginRequestDTO;
import br.app.vizo.controller.request.RefreshRequestDTO;
import br.app.vizo.controller.response.TokenPairDTO;
import br.app.vizo.controller.response.CitizenDTO;
import br.app.vizo.controller.request.RegisterCitizenRequestDTO;
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
    public ResponseEntity<CitizenDTO> registerCitizen(@RequestBody RegisterCitizenRequestDTO body) {
        CitizenDTO response = this.authService.registerCitizen(
                body.document(),
                body.email(),
                body.password(),
                body.name()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPairDTO> login(@RequestBody LoginRequestDTO body) {
        TokenPairDTO response = this.authService.login(body.document(), body.password());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenPairDTO> refresh(@RequestBody RefreshRequestDTO body) {
        TokenPairDTO response = this.authService.refresh(body.token());

        return ResponseEntity.ok(response);
    }
}
