package br.app.vizo.domain.report;

import br.app.vizo.domain.problem.Problem;
import br.app.vizo.domain.user.User;
import br.app.vizo.util.DateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "old_reports")
@Getter
@Setter
@AllArgsConstructor
public class Report {

    @Id
    private UUID id;

    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
    private List<ReportImage> images;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point coordinates;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column(name = "created_at")
    private Instant createdAt;

    public Report() {
        this("", new ArrayList<>(), null, null, null, DateUtil.now());
    }

    public Report(
            String description,
            List<ReportImage> images,
            Point coordinates,
            User user,
            Problem problem,
            Instant createdAt
    ) {
        this(UUID.randomUUID(), description, images, coordinates, user, problem, createdAt);
    }
}
