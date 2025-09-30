package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.NotificationMapper;
import br.app.vizo.core.notification.Notification;
import br.app.vizo.core.notification.NotificationRepository;
import br.app.vizo.core.notification.event.DomainEvent;
import br.app.vizo.infrastructure.persistence.jpa.entity.NotificationEntity;
import br.app.vizo.infrastructure.persistence.jpa.repository.NotificationJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationMapper mapper;
    private final NotificationJpaRepository jpaRepository;

    public NotificationRepositoryImpl(NotificationMapper mapper, NotificationJpaRepository jpaRepository) {
        this.mapper = mapper;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Notification<? extends DomainEvent> save(Notification<? extends DomainEvent> notification) {
        NotificationEntity entity = this.mapper.toEntity(notification);
        NotificationEntity saved = this.jpaRepository.save(entity);
        return this.mapper.toModel(saved);
    }

    @Override
    public void saveAll(List<Notification<? extends DomainEvent>> notifications) {
        List<NotificationEntity> notificationEntities = notifications.stream().map(this.mapper::toEntity)
                .toList();

        this.jpaRepository.saveAll(notificationEntities);
    }

    @Override
    public PageDTO<Notification<DomainEvent>> findAllByRecipientId(UUID id, PaginationDTO pagination) {
        var page = this.jpaRepository.findAllByRecipientId(id, PaginationDTO.resolve(pagination))
                .map(this.mapper::toModel);

        return PageDTO.of(page);
    }

    @Override
    public PageDTO<Notification<DomainEvent>> findAllByRecipientIdAndRead(UUID id, boolean read, PaginationDTO pagination) {
        var page = this.jpaRepository.findAllByRecipientIdAndRead(id, read, PaginationDTO.resolve(pagination))
                .map(this.mapper::toModel);

        return PageDTO.of(page);
    }
}
