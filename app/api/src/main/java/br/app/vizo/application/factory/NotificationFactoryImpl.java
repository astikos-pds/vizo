package br.app.vizo.application.factory;

import br.app.vizo.application.Factory;
import br.app.vizo.core.notification.Notification;
import br.app.vizo.core.notification.NotificationFactory;
import br.app.vizo.core.notification.NotificationType;
import br.app.vizo.core.notification.event.NewProblemEvent;
import br.app.vizo.core.user.User;

@Factory
public class NotificationFactoryImpl implements NotificationFactory {

    @Override
    public Notification<NewProblemEvent> create(User recipient, NewProblemEvent event) {
        return new Notification<>(recipient, NotificationType.NEW_PROBLEM, event);
    }
}
