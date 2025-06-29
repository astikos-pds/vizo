package br.app.vizo.mapper;

import br.app.vizo.controller.response.DepartmentDTO;
import br.app.vizo.domain.department.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(source = "municipality.id", target = "municipalityId")
    @Mapping(source = "createdBy.id", target = "createdById")
    DepartmentDTO toDto(Department department);
}
