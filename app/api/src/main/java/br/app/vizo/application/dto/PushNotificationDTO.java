package br.app.vizo.application.dto;

public record PushNotificationDTO(
        String token,
        String title,
        String body
) {
}
