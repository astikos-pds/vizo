package br.app.vizo.application.usecase.me;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.NotificationDTO;
import br.app.vizo.application.mapper.NotificationMapper;
import br.app.vizo.application.usecase.me.filter.GetMyNotificationsParams;
import br.app.vizo.core.notification.NotificationRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class GetMyNotificationsUseCase {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public List<NotificationDTO<?>> execute(User loggedInUser, GetMyNotificationsParams params) {
        if (params.read() == null) {
            return this.notificationRepository.findAllByRecipientId(loggedInUser.getId())
                    .stream().map(this.notificationMapper::toDto).collect(Collectors.toList());
        }

        return this.notificationRepository.findAllByRecipientIdAndRead(loggedInUser.getId(), params.read())
                .stream().map(this.notificationMapper::toDto).collect(Collectors.toList());
    }
}
