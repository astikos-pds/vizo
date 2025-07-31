package br.app.vizo.application.mapper;

import br.app.vizo.application.Mapper;
import br.app.vizo.application.dto.DepartmentDTO;
import br.app.vizo.application.mapper.base.RepresentationMapper;
import br.app.vizo.core.department.ColorHex;
import br.app.vizo.core.department.Department;
import br.app.vizo.core.department.ProblemScope;
import br.app.vizo.core.shared.Image;
import br.app.vizo.core.shared.MutationTimestamps;
import br.app.vizo.core.shared.Name;
import br.app.vizo.infrastructure.persistence.jpa.entity.DepartmentEntity;

@Mapper
public class DepartmentMapper implements RepresentationMapper<Department, DepartmentEntity, DepartmentDTO> {

    private final MunicipalityMapper municipalityMapper;
    private final AffiliatedUserMapper affiliatedUserMapper;

    public DepartmentMapper(MunicipalityMapper municipalityMapper, AffiliatedUserMapper affiliatedUserMapper) {
        this.municipalityMapper = municipalityMapper;
        this.affiliatedUserMapper = affiliatedUserMapper;
    }

    @Override
    public DepartmentDTO toDto(Department department) {
        return new DepartmentDTO(
                department.getId(),
                this.municipalityMapper.toDto(department.getMunicipality()),
                this.affiliatedUserMapper.toDto(department.getCreator()),
                department.getName(),
                department.getColorHex(),
                department.getIconUrl(),
                department.getScope(),
                department.getCreatedAt(),
                department.getUpdatedAt()
        );
    }

    @Override
    public DepartmentEntity toEntity(Department department) {
        return new DepartmentEntity(
                department.getId(),
                this.municipalityMapper.toEntity(department.getMunicipality()),
                this.affiliatedUserMapper.toEntity(department.getCreator()),
                department.getName(),
                department.getColorHex(),
                department.getIconUrl(),
                department.getScope(),
                department.getCreatedAt(),
                department.getUpdatedAt()
        );
    }

    @Override
    public Department toModel(DepartmentEntity entity) {
        return new Department(
                entity.getId(),
                this.municipalityMapper.toModel(entity.getMunicipality()),
                this.affiliatedUserMapper.toModel(entity.getCreator()),
                new Name(entity.getName()),
                new ColorHex(entity.getColorHex()),
                entity.getIconUrl() == null ? null : new Image(entity.getIconUrl()),
                new ProblemScope(entity.getScope()),
                new MutationTimestamps(entity.getCreatedAt(), entity.getUpdatedAt())
        );
    }
}
