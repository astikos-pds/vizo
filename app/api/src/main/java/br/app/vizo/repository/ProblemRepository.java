package br.app.vizo.repository;

import br.app.vizo.domain.problem.Problem;
import br.app.vizo.domain.problem.ProblemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, UUID> {

    @Query(value = """
            SELECT * FROM problems p
            WHERE ST_DWithin(
                p.coordinates::geography,
                ST_SetSRID(ST_MakePoint(:lat, :lon), 4326)::geography,
                :distance
            )
            LIMIT 1
           """, nativeQuery = true)
    Optional<Problem> findNearestWithinDistance(
            @Param("lat") Double latitude,
            @Param("lon") Double longitude,
            @Param("distance") Double distance
    );

    List<Problem> findAllByValidated(Boolean validated);

    Page<Problem> findAllByTypeIn(Set<ProblemType> types, Pageable pageable);
}
