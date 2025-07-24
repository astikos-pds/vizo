package br.app.vizo.controller;

import br.app.vizo.controller.request.*;
import br.app.vizo.dto.*;
import br.app.vizo.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequestDTO body) {
        UserDTO response = this.authService.register(body);

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

    @PatchMapping("/verification-requests/{id}")
    public ResponseEntity<Void> verifyEmail(@PathVariable UUID id, @RequestBody VerifyCodeRequestDTO body) {
        this.authService.verifyEmail(id, body);

        return ResponseEntity.ok().build();
    }

}
