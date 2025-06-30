package br.app.vizo.mapper;

import br.app.vizo.controller.response.AssignmentDTO;
import br.app.vizo.domain.department.Assignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {

    @Mapping(source = "official.id", target = "officialId")
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "createdBy.id", target = "createdById")
    AssignmentDTO toDTO(Assignment assignment);
}
