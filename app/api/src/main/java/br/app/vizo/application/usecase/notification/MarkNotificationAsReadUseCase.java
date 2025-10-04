package br.app.vizo.application.usecase.notification;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.NotificationDTO;
import br.app.vizo.application.exception.NotAllowedException;
import br.app.vizo.application.exception.NotificationNotFoundException;
import br.app.vizo.application.mapper.NotificationMapper;
import br.app.vizo.core.notification.Notification;
import br.app.vizo.core.notification.NotificationRepository;
import br.app.vizo.core.notification.event.DomainEvent;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class MarkNotificationAsReadUseCase {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationDTO<? extends DomainEvent> execute(User loggedInUser, UUID notificationId) {
        Notification<DomainEvent> notification = this.notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);

        if (!loggedInUser.wasRecipientOf(notification)) {
            throw new NotAllowedException();
        }

        Notification<DomainEvent> updated = loggedInUser.markAsRead(notification);

        Notification<? extends DomainEvent> saved = this.notificationRepository.save(updated);;

        return this.notificationMapper.toDto(saved);
    }
}
