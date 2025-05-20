package br.app.vizo.domain.user.dto;

public record RegisterCitizenRequestDTO(
        String document,
        String email,
        String password,
        String name
) {
}
