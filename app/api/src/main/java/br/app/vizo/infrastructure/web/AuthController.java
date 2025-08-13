package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.EmailVerificationDTO;
import br.app.vizo.application.dto.TokenPairDTO;
import br.app.vizo.application.dto.UserDTO;
import br.app.vizo.application.usecase.auth.*;
import br.app.vizo.application.usecase.auth.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshUseCase refreshUseCase;
    private final RequestEmailVerificationUseCase requestEmailVerificationUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequestDTO body) {
        UserDTO response = this.registerUseCase.execute(body);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPairDTO> login(@RequestBody LoginRequestDTO body) {
        TokenPairDTO response = this.loginUseCase.execute(body);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenPairDTO> refresh(@RequestBody RefreshRequestDTO body) {
        TokenPairDTO response = this.refreshUseCase.execute(body);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/email-verification-requests")
    public ResponseEntity<EmailVerificationDTO> requestEmailVerification(@RequestBody EmailRequestDTO body) {
        EmailVerificationDTO response = this.requestEmailVerificationUseCase.execute(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/email-verification-requests/{id}/verify")
    public ResponseEntity<Void> verifyEmail(@PathVariable UUID id, @RequestBody CodeRequestDTO body) {
        this.verifyEmailUseCase.execute(id, body);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequestDTO body) {
        this.changePasswordUseCase.execute(body);

        return ResponseEntity.ok().build();
    }
}
