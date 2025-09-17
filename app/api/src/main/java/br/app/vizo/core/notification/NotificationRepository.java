package br.app.vizo.core.notification;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository {

    Notification<?> save(Notification<?> notification);

    List<Notification<?>> findAllByRecipientId(UUID id);

    List<Notification<?>> findAllByRecipientIdAndRead(UUID id, boolean read);
}
