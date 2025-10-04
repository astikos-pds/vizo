package br.app.vizo.infrastructure.persistence.jpa.repository;

import br.app.vizo.infrastructure.persistence.jpa.entity.PointOfInterestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PointOfInterestJpaRepository extends JpaRepository<PointOfInterestEntity, UUID> {

    Page<PointOfInterestEntity> findAllByUserId(UUID userId, Pageable pageable);

    @Query(value = """
            SELECT * FROM points_of_interest
            WHERE ST_DWithin(
                coordinates,
                ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography,
                radius
            );
    """, nativeQuery = true)
    List<PointOfInterestEntity> findAllContaining(
            @Param("lat") Double latitude,
            @Param("lon") Double longitude
    );
}
