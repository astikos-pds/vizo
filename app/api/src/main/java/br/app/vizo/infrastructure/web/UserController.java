package br.app.vizo.infrastructure.web;

import br.app.vizo.application.usecase.user.ExistsUserByParamsUseCase;
import br.app.vizo.application.usecase.user.filter.ExistsUserParamsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final ExistsUserByParamsUseCase existsUserByParamsUseCase;

    public UserController(ExistsUserByParamsUseCase existsUserByParamsUseCase) {
        this.existsUserByParamsUseCase = existsUserByParamsUseCase;
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsUserByParams(
            @RequestParam String email,
            @RequestParam(required = false) String document
    ) {
        boolean response = this.existsUserByParamsUseCase.execute(new ExistsUserParamsDTO(email, document));

        return ResponseEntity.ok(response);
    }
}
