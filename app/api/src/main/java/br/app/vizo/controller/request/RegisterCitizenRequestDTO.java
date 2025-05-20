package br.app.vizo.controller.request;

public record RegisterCitizenRequestDTO(
        String document,
        String email,
        String password,
        String name
) {
}
