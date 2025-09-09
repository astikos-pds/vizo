package br.app.vizo.application.mapper;

import br.app.vizo.application.Mapper;
import br.app.vizo.application.dto.PointOfInterestDTO;
import br.app.vizo.application.mapper.base.RepresentationMapper;
import br.app.vizo.core.poi.PointOfInterest;
import br.app.vizo.core.poi.Radius;
import br.app.vizo.core.shared.ColorHex;
import br.app.vizo.core.shared.MutationTimestamps;
import br.app.vizo.core.shared.Name;
import br.app.vizo.core.shared.coordinates.Coordinates;
import br.app.vizo.infrastructure.persistence.jpa.entity.PointOfInterestEntity;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Mapper
public class PointOfInterestMapper implements
        RepresentationMapper<PointOfInterest, PointOfInterestEntity, PointOfInterestDTO>
{

    private final UserMapper userMapper;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    public PointOfInterestMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public PointOfInterestDTO toDto(PointOfInterest pointOfInterest) {
        return new PointOfInterestDTO(
                pointOfInterest.getId(),
                this.userMapper.toDto(pointOfInterest.getUser()),
                pointOfInterest.getName(),
                pointOfInterest.getLatitude(),
                pointOfInterest.getLongitude(),
                pointOfInterest.getRadius(),
                pointOfInterest.getColorHex(),
                pointOfInterest.isActive(),
                pointOfInterest.getCreatedAt(),
                pointOfInterest.getUpdatedAt()
        );
    }

    @Override
    public PointOfInterestEntity toEntity(PointOfInterest pointOfInterest) {
        Point coordinates = this.geometryFactory.createPoint(
                new Coordinate(pointOfInterest.getLongitude(), pointOfInterest.getLatitude())
        );

        return new PointOfInterestEntity(
                pointOfInterest.getId(),
                this.userMapper.toEntity(pointOfInterest.getUser()),
                pointOfInterest.getName(),
                coordinates,
                pointOfInterest.getRadius(),
                pointOfInterest.getColorHex(),
                pointOfInterest.isActive(),
                pointOfInterest.getCreatedAt(),
                pointOfInterest.getUpdatedAt()
        );
    }

    @Override
    public PointOfInterest toModel(PointOfInterestEntity entity) {
        Coordinates coordinates = Coordinates.of(
                entity.getCoordinates().getY(),
                entity.getCoordinates().getX()
        );

        return new PointOfInterest(
                entity.getId(),
                this.userMapper.toModel(entity.getUser()),
                new Name(entity.getName()),
                coordinates,
                new Radius(entity.getRadius()),
                new ColorHex(entity.getColorHex()),
                entity.isActive(),
                new MutationTimestamps(entity.getCreatedAt(), entity.getUpdatedAt())
        );
    }
}
