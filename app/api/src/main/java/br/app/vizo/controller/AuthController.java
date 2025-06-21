package br.app.vizo.controller;

import br.app.vizo.controller.request.LoginRequestDTO;
import br.app.vizo.controller.request.RefreshRequestDTO;
import br.app.vizo.controller.response.OfficialDTO;
import br.app.vizo.controller.response.TokenPairDTO;
import br.app.vizo.controller.response.CitizenDTO;
import br.app.vizo.controller.request.RegisterRequestDTO;
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

    @PostMapping("/citizen/register")
    public ResponseEntity<CitizenDTO> registerAsCitizen(@RequestBody RegisterRequestDTO body) {
        CitizenDTO response = this.authService.registerAsCitizen(body);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/official/register")
    public ResponseEntity<OfficialDTO> registerAsOfficial(@RequestBody RegisterRequestDTO body) {
        OfficialDTO response = this.authService.registerAsOfficial(body);

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
