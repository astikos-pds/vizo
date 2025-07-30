package br.app.vizo.application.mapper;

import br.app.vizo.application.Mapper;
import br.app.vizo.application.dto.MunicipalityDTO;
import br.app.vizo.application.mapper.base.RepresentationMapper;
import br.app.vizo.core.municipality.EmailDomain;
import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.shared.Image;
import br.app.vizo.core.shared.MutationTimestamps;
import br.app.vizo.core.shared.Name;
import br.app.vizo.infrastructure.persistence.jpa.entity.MunicipalityEntity;

@Mapper
public class MunicipalityMapper implements RepresentationMapper<Municipality, MunicipalityEntity, MunicipalityDTO> {

    @Override
    public MunicipalityDTO toDto(Municipality municipality) {
        return new MunicipalityDTO(
                municipality.getId(),
                municipality.getName(),
                municipality.getEmailDomain(),
                municipality.getIconUrl(),
                municipality.getCreatedAt(),
                municipality.getUpdatedAt()
        );
    }

    @Override
    public MunicipalityEntity toEntity(Municipality municipality) {
        return new MunicipalityEntity(
                municipality.getId(),
                municipality.getName(),
                municipality.getEmailDomain(),
                municipality.getIconUrl(),
                municipality.getCreatedAt(),
                municipality.getUpdatedAt()
        );
    }

    @Override
    public Municipality toModel(MunicipalityEntity entity) {
        return new Municipality(
                entity.getId(),
                new Name(entity.getName()),
                new EmailDomain(entity.getEmailDomain()),
                entity.getIconUrl() == null ? null : new Image(entity.getIconUrl()),
                new MutationTimestamps(entity.getCreatedAt(), entity.getUpdatedAt())
        );
    }
}
