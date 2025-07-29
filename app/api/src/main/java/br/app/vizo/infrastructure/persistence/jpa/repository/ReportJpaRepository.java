package br.app.vizo.infrastructure.persistence.jpa.repository;

import br.app.vizo.infrastructure.persistence.jpa.entity.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReportJpaRepository extends JpaRepository<ReportEntity, UUID> {

    Page<ReportEntity> findAllByProblemId(UUID problemId, Pageable pageable);

    Boolean existsByUserIdAndProblemId(UUID userId, UUID problemId);

    Page<ReportEntity> findAllByUserId(UUID userId, Pageable pageable);

    @Query(
            value = """
                SELECT * FROM reports r
                WHERE r.user_id = :user_id
                AND ST_DWithin(
                    r.coordinates::geography,
                    ST_SetSRID(ST_MakePoint(:lat, :lon), 4326)::geography,
                    :distance
                )
                ORDER BY r.created_at DESC
            """,
            countQuery = """
                SELECT COUNT(*) FROM reports r
                WHERE r.user_id = :user_id
                AND ST_DWithin(
                    r.coordinates::geography,
                    ST_SetSRID(ST_MakePoint(:lat, :lon), 4326)::geography,
                    :distance
                )
            """,
            nativeQuery = true
    )
    Page<ReportEntity> findAllByUserIdWithinDistance(
            @Param("user_id") UUID userId,
            @Param("lat") Double latitude,
            @Param("lon") Double longitude,
            @Param("distance") Double distance,
            Pageable pageable
    );
}
