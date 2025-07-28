package br.app.vizo.application.dto;

public record TokenPairDTO(
        String accessToken,
        String refreshToken
) {
}
