package br.app.vizo.application.usecase.auth.request;

public record LoginRequestDTO(
        String document,
        String password
) {
}
