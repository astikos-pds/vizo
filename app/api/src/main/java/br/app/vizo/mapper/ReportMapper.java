package br.app.vizo.mapper;

import br.app.vizo.controller.response.ReportDTO;
import br.app.vizo.domain.report.Report;
import br.app.vizo.domain.report.ReportImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(source = "citizen.id", target = "citizenId")
    @Mapping(source = "problem.id", target = "problemId")
    @Mapping(source = "coordinates.x", target = "latitude")
    @Mapping(source = "coordinates.y", target = "longitude")
    @Mapping(source = "images", target = "imagesUrls")
    ReportDTO toDto(Report report);

    default List<String> map(List<ReportImage> images) {
        if (images == null) {
            return new ArrayList<>();
        }
        return images.stream()
                .map(ReportImage::getUrl)
                .collect(Collectors.toList());
    }
}
