package br.app.vizo.core.report;

import br.app.vizo.core.problem.Problem;
import br.app.vizo.core.shared.Coordinates;
import br.app.vizo.core.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Report {

    @Getter
    private final UUID id;

    @Getter
    private final User user;

    @Getter
    private final Problem problem;

    private final Description description;

    @Getter
    private final Coordinates coordinates;

    private final EvidenceImages images;

    @Getter
    private final Instant createdAt;

    public Report(User user, Problem problem, String description, Double latitude, Double longitude, Set<String> imagesUrls) {
        this(
                UUID.randomUUID(),
                user,
                problem,
                new Description(description),
                Coordinates.of(latitude, longitude),
                EvidenceImages.of(imagesUrls),
                Instant.now()
        );
    }

    public String getDescription() {
        return description.value();
    }

    public Set<String> getImagesUrls() {
        return images.getUrls();
    }
}
