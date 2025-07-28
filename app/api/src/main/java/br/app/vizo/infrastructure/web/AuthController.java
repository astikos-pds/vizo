package br.app.vizo.infrastructure.web;

import br.app.vizo.application.usecase.auth.RegisterUseCase;
import br.app.vizo.application.usecase.auth.request.RegisterRequestDTO;
import br.app.vizo.controller.request.*;
import br.app.vizo.dto.*;
import br.app.vizo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RegisterUseCase registerUseCase;

    @PostMapping("/register")
    public ResponseEntity<br.app.vizo.application.dto.UserDTO> register(@RequestBody RegisterRequestDTO body) {
        br.app.vizo.application.dto.UserDTO response = this.registerUseCase.execute(body);

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
