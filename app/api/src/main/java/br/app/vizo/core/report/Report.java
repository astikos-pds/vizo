package br.app.vizo.core.report;

import br.app.vizo.core.problem.Problem;
import br.app.vizo.core.shared.Coordinates;
import br.app.vizo.core.user.User;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class Report {

    private final UUID id;
    private final User user;
    private final Problem problem;
    private final Description description;
    private final Coordinates coordinates;
    private final EvidenceImages images;
    private final Instant createdAt;

    public Report(User user, Problem problem, String description, Double latitude, Double longitude, Set<String> imagesUrls) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.problem = problem;
        this.description = new Description(description);
        this.coordinates = Coordinates.of(latitude, longitude);
        this.images = EvidenceImages.of(imagesUrls);
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Problem getProblem() {
        return problem;
    }

    public String getDescription() {
        return description.value();
    }

    public Set<String> getImagesUrls() {
        return images.getUrls();
    }

    public Double getLatitude() {
        return coordinates.getLatitude();
    }

    public Double getLongitude() {
        return coordinates.getLongitude();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
