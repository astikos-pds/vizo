package br.app.vizo.domain.problem;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "problems")
@Data
@AllArgsConstructor
public class Problem {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ProblemStatus status;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point coordinates;

    @Column(name = "accumulated_credibility")
    private Double accumulatedCredibility;

    @Column(name = "first_reported_at")
    private Instant firstReportedAt;

    @Column(name = "last_reported_at")
    private Instant lastReportedAt;

    public Problem() {
        this(null, null, 0.0, Instant.now(), Instant.now());
    }

    public Problem(
            ProblemStatus status,
            Point coordinates,
            Double accumulatedCredibility
    ) {
        this(UUID.randomUUID(), status, coordinates, accumulatedCredibility, Instant.now(), Instant.now());
    }

    public Problem(
            ProblemStatus status,
            Point coordinates,
            Double accumulatedCredibility,
            Instant firstReportedAt,
            Instant lastReportedAt
    ) {
        this(UUID.randomUUID(), status, coordinates, accumulatedCredibility, firstReportedAt, lastReportedAt);
    }
}
