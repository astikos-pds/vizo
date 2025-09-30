package br.app.vizo.application.usecase.me;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.NotificationDTO;
import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.NotificationMapper;
import br.app.vizo.application.usecase.me.filter.GetMyNotificationsParams;
import br.app.vizo.core.notification.NotificationRepository;
import br.app.vizo.core.notification.event.DomainEvent;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetMyNotificationsUseCase {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public PageDTO<NotificationDTO<? extends DomainEvent>> execute(
            User loggedInUser,
            PaginationDTO pagination,
            GetMyNotificationsParams params
    ) {
        if (params.read() == null) {
            return this.notificationRepository.findAllByRecipientId(loggedInUser.getId(), pagination)
                    .map(this.notificationMapper::toDto);
        }

        return this.notificationRepository.findAllByRecipientIdAndRead(loggedInUser.getId(), params.read(), pagination)
                .map(this.notificationMapper::toDto);
    }
}
