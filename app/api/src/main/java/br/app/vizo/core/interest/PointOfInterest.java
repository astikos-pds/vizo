package br.app.vizo.core.interest;

import br.app.vizo.core.interest.exception.MustHaveAnOwnerException;
import br.app.vizo.core.shared.coordinates.Coordinates;
import br.app.vizo.core.shared.MutationTimestamps;
import br.app.vizo.core.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PointOfInterest {

    @Getter private final UUID id;
    private final User user;
    @Getter private String name;
    private Coordinates coordinates;
    private Radius radius;
    private final MutationTimestamps timestamps;

    public PointOfInterest(User user, String name, Double latitude, Double longitude, Double radius) {
        if (user == null) {
            throw new MustHaveAnOwnerException();
        }

        this.id = UUID.randomUUID();
        this.user = user;
        this.name = name;
        this.coordinates = Coordinates.of(latitude, longitude);
        this.radius = Radius.of(radius);
        this.timestamps = MutationTimestamps.create();
    }

    public void updateName(String name) {
        this.name = name;
        this.timestamps.update();
    }

    public void updateCoordinates(Double latitude, Double longitude) {
        this.coordinates = Coordinates.of(latitude, longitude);
        this.timestamps.update();
    }

    public void updateRadius(Double radius) {
        this.radius = Radius.of(radius);
        this.timestamps.update();
    }

    public Double getLatitude() {
        return coordinates.getLatitude();
    }

    public Double getLongitude() {
        return coordinates.getLongitude();
    }

    public Double getRadius() {
        return radius.value();
    }

    public Instant wasCreatedAt() {
        return this.timestamps.getCreatedAt();
    }

    public Instant wasUpdatedAt() {
        return this.timestamps.getUpdatedAt();
    }

    public boolean isOwnedBy(User candidate) {
        return user.isSameAs(candidate);
    }
}
