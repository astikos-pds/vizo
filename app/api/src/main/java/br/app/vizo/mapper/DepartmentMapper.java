package br.app.vizo.mapper;

import br.app.vizo.dto.DepartmentDTO;
import br.app.vizo.domain.department.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MunicipalityMapper.class, OfficialMapper.class})
public interface DepartmentMapper extends DtoMapper<Department, DepartmentDTO> {
}
