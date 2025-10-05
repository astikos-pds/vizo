package br.app.vizo.application.listener;

import br.app.vizo.application.dto.PushNotificationDTO;
import br.app.vizo.application.service.PushNotificationService;
import br.app.vizo.core.notification.Notification;
import br.app.vizo.core.notification.NotificationFactory;
import br.app.vizo.core.notification.NotificationRepository;
import br.app.vizo.core.notification.event.DomainEvent;
import br.app.vizo.core.notification.event.NewProblemEvent;
import br.app.vizo.core.poi.PointOfInterest;
import br.app.vizo.core.poi.PointOfInterestRepository;
import br.app.vizo.core.shared.coordinates.Coordinates;
import br.app.vizo.core.user.User;
import br.app.vizo.core.user.push.PushToken;
import br.app.vizo.core.user.push.PushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NewProblemEventListener {

    private final PointOfInterestRepository pointOfInterestRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationFactory notificationFactory;
    private final PushTokenRepository pushTokenRepository;
    private final PushNotificationService pushNotificationService;

    @EventListener
    public void handleEvent(NewProblemEvent event) {
        List<PointOfInterest> pointOfInterests = this.pointOfInterestRepository
                .findAllContaining(Coordinates.of(event.problemLatitude(), event.problemLongitude()));

        Set<UUID> usersIds = pointOfInterests.stream().map(PointOfInterest::getUserId)
                .collect(Collectors.toSet());

        Map<UUID, PushToken> pushTokensByUserId = this.pushTokenRepository.findAllByUserIdIn(usersIds).stream()
                .collect(Collectors.toMap(PushToken::getUserId, token -> token));

        List<Notification<? extends DomainEvent>> notifications = new LinkedList<>();
        List<PushNotificationDTO> pushNotifications = new LinkedList<>();

        for (PointOfInterest poi : pointOfInterests) {
            User user = poi.getUser();

            Notification<NewProblemEvent> notification = this.notificationFactory.create(user, event);
            notifications.add(notification);

            PushToken pushToken = pushTokensByUserId.get(notification.getRecipient().getId());
            String title = "New problem around %s".formatted(poi.getName());
            String description = "A user reported %s near %s: '%s'"
                    .formatted(event.problemType(), poi.getName(), event.firstReportDescription());

            PushNotificationDTO pushNotification = new PushNotificationDTO(pushToken.getToken(), title, description);
            pushNotifications.add(pushNotification);
        }

        this.notificationRepository.saveAll(notifications);
        this.pushNotificationService.sendAll(pushNotifications);
    }
}
