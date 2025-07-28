package br.app.vizo.mapper;

import br.app.vizo.dto.ReportDTO;
import br.app.vizo.domain.report.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OldUserMapper.class, OldProblemMapper.class, OldReportImageMapper.class})
public interface OldReportMapper extends DtoMapper<Report, ReportDTO> {

    @Mapping(source = "coordinates.x", target = "latitude")
    @Mapping(source = "coordinates.y", target = "longitude")
    ReportDTO toDto(Report report);
}
