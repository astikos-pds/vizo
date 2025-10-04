package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.NotificationDTO;
import br.app.vizo.application.usecase.notification.MarkNotificationAsReadUseCase;
import br.app.vizo.config.security.UserDetailsImpl;
import br.app.vizo.core.notification.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final MarkNotificationAsReadUseCase markNotificationAsReadUseCase;

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationDTO<? extends DomainEvent>> markNotificationAsRead(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID id
    ) {
        NotificationDTO<? extends DomainEvent> response =
                this.markNotificationAsReadUseCase.execute(userDetails.getUser(), id);

        return ResponseEntity.ok(response);
    }
}
