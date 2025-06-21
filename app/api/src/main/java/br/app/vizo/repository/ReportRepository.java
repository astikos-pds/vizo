package br.app.vizo.repository;

import br.app.vizo.domain.report.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    List<Report> findByProblemId(UUID problemId, Sort sort);

    Boolean existsByProblemIdAndCitizenId(UUID problemId, UUID citizenId);

    Page<Report> findAllByCitizenId(UUID citizenId, Pageable pageable);

    @Query(
            value = """
                SELECT * FROM reports r
                WHERE r.citizen_id = :citizen_id
                AND ST_DWithin(
                    r.coordinates::geography,
                    ST_SetSRID(ST_MakePoint(:lat, :lon), 4326)::geography,
                    :distance
                )
                ORDER BY r.created_at DESC
            """,
            countQuery = """
                SELECT COUNT(*) FROM reports r
                WHERE r.citizen_id = :citizen_id
                AND ST_DWithin(
                    r.coordinates::geography,
                    ST_SetSRID(ST_MakePoint(:lat, :lon), 4326)::geography,
                    :distance
                )
            """,
            nativeQuery = true
    )
    Page<Report> findAllByCitizenIdWithinDistance(
            @Param("citizen_id") UUID citizenId,
            @Param("lat") Double latitude,
            @Param("lon") Double longitude,
            @Param("distance") Double distance,
            Pageable pageable
    );
}
