package br.app.vizo.repository;

import br.app.vizo.domain.report.Report;
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

    List<Report> findByCitizenId(UUID citizenId, Sort sort);

    @Query(value = """
            SELECT * FROM reports r
            WHERE ST_DWithin(
                r.coordinates::geography,
                ST_SetSRID(ST_MakePoint(:lat, :lon), 4326)::geography,
                :distance
            ) AND r.citizen_id = :citizen_id
            ORDER BY r.created_at DESC
           """, nativeQuery = true)
    List<Report> findByCitizenIdWithinDistance(
            @Param("citizen_id") UUID citizenId,
            @Param("lat") Double latitude,
            @Param("lon") Double longitude,
            @Param("distance") Double distance
    );
}
