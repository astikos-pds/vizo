package br.app.vizo.core.notification;

import br.app.vizo.core.notification.event.NewProblemEvent;
import br.app.vizo.core.user.User;

public interface NotificationFactory {

    Notification<NewProblemEvent> create(User recipient, NewProblemEvent event);
}
