package br.app.vizo.controller.request;

public record LoginRequestDTO(
        String document,
        String password
) {
}
