package br.app.vizo.application.service;

import br.app.vizo.core.notification.Notification;
import br.app.vizo.core.notification.NotificationFactory;
import br.app.vizo.core.notification.NotificationRepository;
import br.app.vizo.core.notification.event.DomainEvent;
import br.app.vizo.core.notification.event.NewProblemEvent;
import br.app.vizo.core.poi.PointOfInterest;
import br.app.vizo.core.poi.PointOfInterestRepository;
import br.app.vizo.core.shared.coordinates.Coordinates;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final PointOfInterestRepository pointOfInterestRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationFactory notificationFactory;


    @EventListener
    public void handleNewProblem(NewProblemEvent event) {
        List<PointOfInterest> pointOfInterests = this.pointOfInterestRepository
                .findAllContaining(Coordinates.of(event.problemLatitude(), event.problemLongitude()));

        List<Notification<? extends DomainEvent>> notifications = new LinkedList<>();

        for (PointOfInterest poi : pointOfInterests) {
            Notification<NewProblemEvent> notification = this.notificationFactory.create(poi.getUser(), event);

            notifications.add(notification);
        }

        this.notificationRepository.saveAll(notifications);
    }
}
