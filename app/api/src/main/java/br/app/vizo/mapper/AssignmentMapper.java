package br.app.vizo.mapper;

import br.app.vizo.dto.AssignmentDTO;
import br.app.vizo.domain.department.Assignment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, DepartmentMapper.class})
public interface AssignmentMapper extends DtoMapper<Assignment, AssignmentDTO> {
}
