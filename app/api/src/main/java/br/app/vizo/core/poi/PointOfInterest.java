package br.app.vizo.core.poi;

import br.app.vizo.core.poi.exception.InvalidPointOfInterestException;
import br.app.vizo.core.shared.Name;
import br.app.vizo.core.shared.coordinates.Coordinates;
import br.app.vizo.core.shared.MutationTimestamps;
import br.app.vizo.core.user.User;

import java.time.Instant;
import java.util.UUID;

public class PointOfInterest {

    private final UUID id;
    private final User user;
    private Name name;
    private Coordinates coordinates;
    private Radius radius;
    private final MutationTimestamps timestamps;

    public PointOfInterest(UUID id, User user, Name name, Coordinates coordinates, Radius radius, MutationTimestamps timestamps) {
        if (id == null || user == null || name == null || coordinates == null || radius == null || timestamps == null) {
            throw new InvalidPointOfInterestException();
        }

        this.id = id;
        this.user = user;
        this.name = name;
        this.coordinates = coordinates;
        this.radius = radius;
        this.timestamps = timestamps;
    }

    public void update(Name name, Coordinates coordinates, Radius radius) {
        this.name = name;
        this.coordinates = coordinates;
        this.radius = radius;

        this.timestamps.update();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public User getUser() {
        return user;
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

    public Instant getCreatedAt() {
        return this.timestamps.getCreatedAt();
    }

    public Instant getUpdatedAt() {
        return this.timestamps.getUpdatedAt();
    }

    public boolean isOwnedBy(User candidate) {
        return user.isSameAs(candidate);
    }
}
