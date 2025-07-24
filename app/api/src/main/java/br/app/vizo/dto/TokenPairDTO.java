package br.app.vizo.dto;

public record TokenPairDTO(
        String accessToken,
        String refreshToken
) {
}
