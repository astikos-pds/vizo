package br.app.vizo.controller.response;

public record TokenPairDTO(
        String accessToken,
        String refreshToken
) {
}
