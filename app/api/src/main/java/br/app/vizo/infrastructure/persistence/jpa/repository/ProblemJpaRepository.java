package br.app.vizo.infrastructure.persistence.jpa.repository;


import br.app.vizo.core.problem.ProblemType;
import br.app.vizo.infrastructure.persistence.jpa.entity.ProblemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProblemJpaRepository extends JpaRepository<ProblemEntity, UUID> {

    @Query(value = """
            SELECT * FROM problems p
            WHERE p.status != 'RESOLVED'
            AND p.type = :type
            AND ST_DWithin(
                p.coordinates::geography,
                ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography,
                :distance
            )
            ORDER BY ST_Distance(
                p.coordinates::geography,
                ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography
            )
            LIMIT 1
    """, nativeQuery = true)
    Optional<ProblemEntity> findClosestUnresolvedByTypeWithinRadiusInMeters(
            @Param("type") String problemType,
            @Param("lat") Double latitude,
            @Param("lon") Double longitude,
            @Param("distance") Double radiusInMeters
    );

    Page<ProblemEntity> findAllByTypeIn(Set<ProblemType> types, Pageable pageable);
}
