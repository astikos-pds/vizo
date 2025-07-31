package br.app.vizo.application.mapper;

import br.app.vizo.application.Mapper;
import br.app.vizo.application.dto.AffiliatedUserDTO;
import br.app.vizo.application.mapper.base.RepresentationMapper;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.shared.Email;
import br.app.vizo.infrastructure.persistence.jpa.entity.AffiliatedUserEntity;

@Mapper
public class AffiliatedUserMapper implements
        RepresentationMapper<AffiliatedUser, AffiliatedUserEntity, AffiliatedUserDTO>
{

    private final UserMapper userMapper;
    private final MunicipalityMapper municipalityMapper;

    public AffiliatedUserMapper(UserMapper userMapper, MunicipalityMapper municipalityMapper) {
        this.userMapper = userMapper;
        this.municipalityMapper = municipalityMapper;
    }

    @Override
    public AffiliatedUserDTO toDto(AffiliatedUser affiliatedUser) {
        return new AffiliatedUserDTO(
                affiliatedUser.getId(),
                this.userMapper.toDto(affiliatedUser.getUser()),
                this.municipalityMapper.toDto(affiliatedUser.getMunicipality()),
                affiliatedUser.getInstitutionalEmail(),
                affiliatedUser.isAdmin(),
                affiliatedUser.getStatus(),
                affiliatedUser.getAffiliatedAt(),
                this.toDto(affiliatedUser.getApprover()),
                affiliatedUser.getApprovedAt()
        );
    }

    @Override
    public AffiliatedUserEntity toEntity(AffiliatedUser affiliatedUser) {
        return new AffiliatedUserEntity(
                affiliatedUser.getId(),
                this.userMapper.toEntity(affiliatedUser.getUser()),
                this.municipalityMapper.toEntity(affiliatedUser.getMunicipality()),
                affiliatedUser.getInstitutionalEmail(),
                affiliatedUser.isAdmin(),
                affiliatedUser.getStatus(),
                affiliatedUser.getAffiliatedAt(),
                this.toEntity(affiliatedUser.getApprover()),
                affiliatedUser.getApprovedAt()
        );
    }

    @Override
    public AffiliatedUser toModel(AffiliatedUserEntity entity) {
        return new AffiliatedUser(
                entity.getId(),
                this.userMapper.toModel(entity.getUser()),
                this.municipalityMapper.toModel(entity.getMunicipality()),
                new Email(entity.getInstitutionalEmail()),
                entity.isAdmin(),
                entity.getStatus(),
                entity.getAffiliatedAt(),
                this.toModel(entity.getApprover()),
                entity.getApprovedAt()
        );
    }
}
