package br.app.vizo.mapper;

import br.app.vizo.controller.response.ReportDTO;
import br.app.vizo.domain.report.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CitizenMapper.class, ProblemMapper.class, ReportImageMapper.class})
public interface ReportMapper extends DtoMapper<Report, ReportDTO> {

    @Mapping(source = "coordinates.x", target = "latitude")
    @Mapping(source = "coordinates.y", target = "longitude")
    ReportDTO toDto(Report report);
}
