package br.app.vizo.domain.report;

import br.app.vizo.domain.problem.Problem;
import br.app.vizo.domain.user.Citizen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Data
@AllArgsConstructor
public class Report {

    @Id
    private UUID id;

    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point coordinates;

    @ManyToOne
    @JoinColumn(name = "citizen_id")
    private Citizen citizen;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column(name = "created_at")
    private Instant createdAt;

    public Report() {
        this("", "", null, null, null, Instant.now());
    }

    public Report(
            String description,
            String imageUrl,
            Point coordinates,
            Citizen citizen,
            Problem problem,
            Instant createdAt
    ) {
        this(UUID.randomUUID(), description, imageUrl, coordinates, citizen, problem, createdAt);
    }
}
