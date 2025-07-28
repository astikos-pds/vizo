package br.app.vizo.mapper;

import br.app.vizo.dto.AssignmentDTO;
import br.app.vizo.domain.department.Assignment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OldUserMapper.class, OldDepartmentMapper.class})
public interface OldAssignmentMapper extends DtoMapper<Assignment, AssignmentDTO> {
}
