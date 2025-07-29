package br.app.vizo.infrastructure.persistence.jpa.entity;

import br.app.vizo.core.problem.ProblemStatus;
import br.app.vizo.core.problem.ProblemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "problems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemEntity {

    @Id
    private UUID id;

    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point coordinates;

    @Enumerated(EnumType.STRING)
    private ProblemType type;

    @Enumerated(EnumType.STRING)
    private ProblemStatus status;

    @Column(name = "accumulated_credibility", nullable = false)
    private Double accumulatedCredibility;

    @Column(nullable = false)
    private boolean validated;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "first_reported_at", nullable = false)
    private Instant firstReportedAt;

    @Column(name = "last_reported_at", nullable = false)
    private Instant lastReportedAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

}
