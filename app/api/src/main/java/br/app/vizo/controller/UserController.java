package br.app.vizo.controller;

import br.app.vizo.controller.response.profile.ProfileDTO;
import br.app.vizo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
