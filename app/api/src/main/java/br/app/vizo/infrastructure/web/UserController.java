package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.UserDTO;
import br.app.vizo.application.usecase.user.GetUserByParamsUseCase;
import br.app.vizo.application.usecase.user.filter.GetUserParamsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final GetUserByParamsUseCase getUserByParamsUseCase;

    public UserController(GetUserByParamsUseCase getUserByParamsUseCase) {
        this.getUserByParamsUseCase = getUserByParamsUseCase;
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUserByParams(
            @RequestParam String email,
            @RequestParam(required = false) String document
    ) {
        UserDTO response = this.getUserByParamsUseCase.execute(new GetUserParamsDTO(email, document));

        return ResponseEntity.ok(response);
    }
}
