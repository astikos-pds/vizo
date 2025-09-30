package br.app.vizo.core.notification;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.core.notification.event.DomainEvent;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository {

    Notification<? extends DomainEvent> save(Notification<? extends DomainEvent> notification);

    void saveAll(List<Notification<? extends DomainEvent>> notifications);

    PageDTO<Notification<DomainEvent>> findAllByRecipientId(UUID id, PaginationDTO pagination);

    PageDTO<Notification<DomainEvent>> findAllByRecipientIdAndRead(UUID id, boolean read, PaginationDTO pagination);
}
