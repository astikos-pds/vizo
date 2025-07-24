package br.app.vizo.mapper;

import br.app.vizo.dto.ProblemDTO;
import br.app.vizo.domain.problem.Problem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProblemMapper extends DtoMapper<Problem, ProblemDTO> {

    @Mapping(source = "coordinates.x", target = "latitude")
    @Mapping(source = "coordinates.y", target = "longitude")
    ProblemDTO toDto(Problem problem);
}
