package br.app.vizo.mapper;

import br.app.vizo.dto.DepartmentDTO;
import br.app.vizo.domain.department.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OldMunicipalityMapper.class, OldUserMapper.class})
public interface OldDepartmentMapper extends DtoMapper<Department, DepartmentDTO> {
}
