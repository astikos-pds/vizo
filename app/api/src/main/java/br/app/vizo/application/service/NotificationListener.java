package br.app.vizo.application.service;

import br.app.vizo.core.notification.Notification;
import br.app.vizo.core.notification.NotificationRepository;
import br.app.vizo.core.notification.NotificationType;
import br.app.vizo.core.notification.event.NewProblemEvent;
import br.app.vizo.core.poi.PointOfInterest;
import br.app.vizo.core.poi.PointOfInterestRepository;
import br.app.vizo.core.shared.coordinates.Coordinates;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final PointOfInterestRepository pointOfInterestRepository;
    private final NotificationRepository notificationRepository;


    @EventListener
    public void handleNewProblem(NewProblemEvent event) {
        List<PointOfInterest> pointOfInterests = this.pointOfInterestRepository
                .findAllContaining(Coordinates.of(event.problemLatitude(), event.problemLongitude()));

        for (PointOfInterest poi : pointOfInterests) {
            Notification<NewProblemEvent> notification = new Notification<>(
                    poi.getUser(),
                    NotificationType.NEW_PROBLEM,
                    event
            );

            this.notificationRepository.save(notification);
        }
    }
}
