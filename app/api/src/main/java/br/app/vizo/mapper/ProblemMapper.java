package br.app.vizo.mapper;

import br.app.vizo.controller.response.ProblemDTO;
import br.app.vizo.domain.problem.Problem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProblemMapper {

    @Mapping(source = "coordinates.x", target = "latitude")
    @Mapping(source = "coordinates.y", target = "longitude")
    ProblemDTO toDto(Problem problem);
}
