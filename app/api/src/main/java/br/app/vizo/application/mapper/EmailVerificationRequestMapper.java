package br.app.vizo.application.mapper;

import br.app.vizo.application.Mapper;
import br.app.vizo.application.dto.EmailVerificationDTO;
import br.app.vizo.application.mapper.base.RepresentationMapper;
import br.app.vizo.core.verification.EmailVerificationRequest;
import br.app.vizo.infrastructure.persistence.jpa.entity.EmailVerificationRequestEntity;

@Mapper
public class EmailVerificationRequestMapper implements
        RepresentationMapper<EmailVerificationRequest, EmailVerificationRequestEntity, EmailVerificationDTO>
{
    @Override
    public EmailVerificationDTO toDto(EmailVerificationRequest model) {
        return new EmailVerificationDTO(
                model.getId(),
                model.getCodeLength(),
                model.getExpiresAt(),
                model.getCreatedAt()
        );
    }

    @Override
    public EmailVerificationRequestEntity toEntity(EmailVerificationRequest model) {
        return new EmailVerificationRequestEntity(
                model.getId(),
                model.getEmail(),
                model.getCode(),
                model.isVerified(),
                model.getExpiresAt(),
                model.getCreatedAt()
        );
    }

    @Override
    public EmailVerificationRequest toModel(EmailVerificationRequestEntity entity) {
        return new EmailVerificationRequest(
                entity.getId(),
                entity.getEmail(),
                entity.getCode(),
                entity.getVerified(),
                entity.getExpiresAt(),
                entity.getCreatedAt()
        );
    }
}
