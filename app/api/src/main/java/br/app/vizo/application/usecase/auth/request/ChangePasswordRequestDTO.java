package br.app.vizo.application.usecase.auth.request;

public record ChangePasswordRequestDTO(
        String email,
        String newPassword
) {
}
