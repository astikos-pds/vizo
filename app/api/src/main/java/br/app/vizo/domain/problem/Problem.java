package br.app.vizo.domain.problem;

import br.app.vizo.util.DateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "old_problems")
@Getter
@Setter
@AllArgsConstructor
public class Problem {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ProblemStatus status;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point coordinates;

    @Enumerated(EnumType.STRING)
    private ProblemType type;

    @Column(name = "accumulated_credibility")
    private Double accumulatedCredibility;

    private Boolean validated;

    @Column(name = "first_reported_at")
    private Instant firstReportedAt;

    @Column(name = "last_reported_at")
    private Instant lastReportedAt;

    public Problem() {
        this(null, null, ProblemType.OTHER, 0.0);
    }

    public Problem(
            ProblemStatus status,
            Point coordinates,
            ProblemType type,
            Double accumulatedCredibility
    ) {
        this(status, coordinates, type, accumulatedCredibility, false, DateUtil.now(), DateUtil.now());
    }

    public Problem(
            ProblemStatus status,
            Point coordinates,
            ProblemType type,
            Double accumulatedCredibility,
            Boolean validated,
            Instant firstReportedAt,
            Instant lastReportedAt
    ) {
        this(UUID.randomUUID(), status, coordinates, type, accumulatedCredibility, validated, firstReportedAt, lastReportedAt);
    }
}
