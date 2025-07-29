package br.app.vizo.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportEntity {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private ProblemEntity problem;

    @Column(nullable = false)
    private String description;

    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point coordinates;

    @ElementCollection
    @CollectionTable(name = "report_images", joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "image_url")
    private Set<String> imagesUrls = new HashSet<>();

    @Column(nullable = false)
    private Double credibility;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}
