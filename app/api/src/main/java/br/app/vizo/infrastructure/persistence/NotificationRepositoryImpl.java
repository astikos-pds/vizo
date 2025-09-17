package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.mapper.NotificationMapper;
import br.app.vizo.core.notification.Notification;
import br.app.vizo.core.notification.NotificationRepository;
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
    public Notification<?> save(Notification<?> notification) {
        NotificationEntity entity = this.mapper.toEntity(notification);
        NotificationEntity saved = this.jpaRepository.save(entity);
        return this.mapper.toModel(saved);
    }

    @Override
    public List<Notification<?>> findAllByRecipientId(UUID id) {
        return this.jpaRepository.findAllByRecipientId(id)
                .stream()
                .map(this.mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification<?>> findAllByRecipientIdAndRead(UUID id, boolean read) {
        return this.jpaRepository.findAllByRecipientIdAndRead(id, read)
                .stream()
                .map(this.mapper::toModel)
                .collect(Collectors.toList());
    }
}
