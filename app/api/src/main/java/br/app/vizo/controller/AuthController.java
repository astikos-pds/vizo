package br.app.vizo.controller;

import br.app.vizo.controller.request.EmailRequestDTO;
import br.app.vizo.controller.request.LoginRequestDTO;
import br.app.vizo.controller.request.RefreshRequestDTO;
import br.app.vizo.controller.response.EmailVerificationDTO;
import br.app.vizo.controller.response.OfficialDTO;
import br.app.vizo.controller.response.TokenPairDTO;
import br.app.vizo.controller.response.CitizenDTO;
import br.app.vizo.controller.request.RegisterRequestDTO;
import br.app.vizo.service.auth.CitizenAuthService;
import br.app.vizo.service.auth.AuthService;
import br.app.vizo.service.auth.OfficialAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CitizenAuthService citizenAuthService;
    private final OfficialAuthService officialAuthService;

    public AuthController(
            AuthService authService,
            CitizenAuthService citizenAuthService,
            OfficialAuthService officialAuthService
    ) {
        this.authService = authService;
        this.citizenAuthService = citizenAuthService;
        this.officialAuthService = officialAuthService;
    }


    @PostMapping("/citizen/register")
    public ResponseEntity<CitizenDTO> registerAsCitizen(@RequestBody RegisterRequestDTO body) {
        CitizenDTO response = this.citizenAuthService.registerAsCitizen(body);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/official/register")
    public ResponseEntity<OfficialDTO> registerAsOfficial(@RequestBody RegisterRequestDTO body) {
        OfficialDTO response = this.officialAuthService.registerAsOfficial(body);

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

    @PostMapping("/verification-requests")
    public ResponseEntity<EmailVerificationDTO> createVerificationRequest(@RequestBody EmailRequestDTO body) {
        EmailVerificationDTO response = this.authService.createVerificationRequest(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
