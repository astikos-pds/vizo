package br.app.vizo.mapper;

import br.app.vizo.controller.response.AssignmentDTO;
import br.app.vizo.domain.department.Assignment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OfficialMapper.class, DepartmentMapper.class})
public interface AssignmentMapper extends DtoMapper<Assignment, AssignmentDTO> {
}
