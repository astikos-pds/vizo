package br.app.vizo.application.mapper;

import br.app.vizo.application.Mapper;
import br.app.vizo.application.dto.ProblemDTO;
import br.app.vizo.application.mapper.base.RepresentationMapper;
import br.app.vizo.core.problem.Problem;
import br.app.vizo.core.problem.ReportingTimeline;
import br.app.vizo.core.shared.Coordinates;
import br.app.vizo.core.shared.Credibility;
import br.app.vizo.core.shared.MutationTimestamps;
import br.app.vizo.infrastructure.persistence.jpa.entity.ProblemEntity;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Mapper
public class ProblemMapper implements RepresentationMapper<Problem, ProblemEntity, ProblemDTO> {

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Override
    public ProblemDTO toDto(Problem problem) {
        return new ProblemDTO(
                problem.getId(),
                problem.getLatitude(),
                problem.getLongitude(),
                problem.getType(),
                problem.getStatus(),
                problem.getAccumulatedCredibility(),
                problem.isValidated(),
                problem.getCreatedAt(),
                problem.getUpdatedAt(),
                problem.getFirstReportedAt(),
                problem.getLastReportedAt(),
                problem.getResolvedAt()
        );
    }

    @Override
    public ProblemEntity toEntity(Problem problem) {
        Point point = this.geometryFactory.createPoint(new Coordinate(problem.getLatitude(), problem.getLongitude()));

        return new ProblemEntity(
                problem.getId(),
                point,
                problem.getType(),
                problem.getStatus(),
                problem.getAccumulatedCredibility(),
                problem.isValidated(),
                problem.getCreatedAt(),
                problem.getUpdatedAt(),
                problem.getFirstReportedAt(),
                problem.getLastReportedAt(),
                problem.getResolvedAt()
        );
    }

    @Override
    public Problem toModel(ProblemEntity entity) {
        Coordinates coordinates = Coordinates.of(
                entity.getCoordinates().getY(),
                entity.getCoordinates().getX()
        );

        return new Problem(
                entity.getId(),
                coordinates,
                entity.getType(),
                entity.getStatus(),
                new Credibility(entity.getAccumulatedCredibility()),
                entity.isValidated(),
                new MutationTimestamps(entity.getCreatedAt(), entity.getUpdatedAt()),
                new ReportingTimeline(entity.getFirstReportedAt(), entity.getLastReportedAt()),
                entity.getResolvedAt()
        );
    }
}
