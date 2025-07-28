package br.app.vizo.application.usecase.auth.request;

public record RegisterRequestDTO(
        String document,
        String email,
        String password,
        String name
) {
}
