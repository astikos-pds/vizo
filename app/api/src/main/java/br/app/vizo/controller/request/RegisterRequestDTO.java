package br.app.vizo.controller.request;

public record RegisterRequestDTO(
        String document,
        String email,
        String password,
        String name
) {
}
