package br.app.vizo.core.poi;

import br.app.vizo.core.shared.ColorHex;
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
    private ColorHex colorHex;
    private boolean active;
    private final MutationTimestamps timestamps;

    public PointOfInterest(UUID id, User user, Name name, Coordinates coordinates, Radius radius, ColorHex colorHex, boolean active, MutationTimestamps timestamps) {
        if (id == null || user == null || name == null || coordinates == null || radius == null || colorHex == null || timestamps == null) {
            throw new InvalidPointOfInterestException();
        }

        this.id = id;
        this.user = user;
        this.name = name;
        this.coordinates = coordinates;
        this.radius = radius;
        this.colorHex = colorHex;
        this.active = active;
        this.timestamps = timestamps;
    }

    public PointOfInterest(User user, Name name, Coordinates coordinates, Radius radius, ColorHex colorHex, boolean active) {
        this(
                UUID.randomUUID(),
                user,
                name,
                coordinates,
                radius,
                colorHex,
                active,
                new MutationTimestamps()
        );
    }

    public void update(Name name, Coordinates coordinates, Radius radius, ColorHex colorHex, boolean active) {
        this.name = name;
        this.coordinates = coordinates;
        this.radius = radius;
        this.colorHex = colorHex;
        this.active = active;

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

    public String getColorHex() {
        return colorHex.value();
    }

    public boolean isActive() {
        return active;
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
