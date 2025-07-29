package br.app.vizo.application.mapper;

import br.app.vizo.application.Mapper;
import br.app.vizo.application.dto.ReportDTO;
import br.app.vizo.application.mapper.base.RepresentationMapper;
import br.app.vizo.core.report.Description;
import br.app.vizo.core.report.EvidenceImages;
import br.app.vizo.core.report.Report;
import br.app.vizo.core.shared.Coordinates;
import br.app.vizo.core.shared.Credibility;
import br.app.vizo.infrastructure.persistence.jpa.entity.ReportEntity;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Mapper
public class ReportMapper implements RepresentationMapper<Report, ReportEntity, ReportDTO> {

    private final UserMapper userMapper;
    private final ProblemMapper problemMapper;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    public ReportMapper(UserMapper userMapper, ProblemMapper problemMapper) {
        this.userMapper = userMapper;
        this.problemMapper = problemMapper;
    }

    @Override
    public ReportDTO toDto(Report report) {
        return new ReportDTO(
                report.getId(),
                this.userMapper.toDto(report.getUser()),
                this.problemMapper.toDto(report.getProblem()),
                report.getDescription(),
                report.getLatitude(),
                report.getLongitude(),
                report.getImagesUrls(),
                report.getCredibility(),
                report.getCreatedAt()
        );
    }

    @Override
    public ReportEntity toEntity(Report report) {
        Point point = this.geometryFactory.createPoint(
                new Coordinate(report.getLongitude(), report.getLatitude())
        );

        return new ReportEntity(
                report.getId(),
                this.userMapper.toEntity(report.getUser()),
                this.problemMapper.toEntity(report.getProblem()),
                report.getDescription(),
                point,
                report.getImagesUrls(),
                report.getCredibility(),
                report.getCreatedAt()
        );
    }

    @Override
    public Report toModel(ReportEntity entity) {
        Coordinates coordinates = Coordinates.of(
                entity.getCoordinates().getY(),
                entity.getCoordinates().getX()
        );

        return new Report(
                entity.getId(),
                this.userMapper.toModel(entity.getUser()),
                this.problemMapper.toModel(entity.getProblem()),
                new Description(entity.getDescription()),
                coordinates,
                EvidenceImages.of(entity.getImagesUrls()),
                new Credibility(entity.getCredibility()),
                entity.getCreatedAt()
        );
    }
}
