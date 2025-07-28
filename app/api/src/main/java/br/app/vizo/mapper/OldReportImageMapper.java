package br.app.vizo.mapper;

import br.app.vizo.dto.ReportImageDTO;
import br.app.vizo.domain.report.ReportImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OldReportImageMapper extends DtoMapper<ReportImage, ReportImageDTO> {
}
