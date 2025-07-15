package br.app.vizo.mapper;

import br.app.vizo.controller.response.ReportImageDTO;
import br.app.vizo.domain.report.ReportImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportImageMapper extends DtoMapper<ReportImage, ReportImageDTO> {
}
