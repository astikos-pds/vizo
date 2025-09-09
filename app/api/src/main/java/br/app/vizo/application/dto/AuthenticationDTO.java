package br.app.vizo.application.dto;

public record AuthenticationDTO(
        String accessToken,
        String refreshToken,
        UserDTO user
) {
}
