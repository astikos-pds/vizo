package br.app.vizo.core.department;

import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.department.exception.InvalidDepartmentException;
import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.problem.Problem;
import br.app.vizo.core.problem.ProblemType;
import br.app.vizo.core.shared.ColorHex;
import br.app.vizo.core.shared.Image;
import br.app.vizo.core.shared.MutationTimestamps;
import br.app.vizo.core.shared.Name;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class Department {

    private final UUID id;
    private final Municipality municipality;
    private final AffiliatedUser creator;
    private Name name;
    private ColorHex colorHex;
    private Image icon;
    private ProblemScope scope;
    private final MutationTimestamps timestamps;

    public Department(UUID id, Municipality municipality, AffiliatedUser creator, Name name, ColorHex colorHex, Image icon, ProblemScope scope, MutationTimestamps timestamps) {
        if (municipality == null || creator == null || name == null || colorHex == null || scope == null || timestamps == null) {
            throw new InvalidDepartmentException();
        }

        this.id = id;
        this.municipality = municipality;
        this.creator = creator;
        this.name = name;
        this.colorHex = colorHex;
        this.icon = icon;
        this.scope = scope;
        this.timestamps = timestamps;
    }

    public Department(Municipality municipality, AffiliatedUser creator, String name, String colorHex, String iconUrl, Set<ProblemType> problemTypes) {
        this(
                UUID.randomUUID(),
                municipality,
                creator,
                new Name(name),
                new ColorHex(colorHex),
                iconUrl == null ? null : new Image(iconUrl),
                new ProblemScope(problemTypes),
                MutationTimestamps.create()
        );
    }

    public void update(String name, String colorHex, String iconUrl, Set<ProblemType> problemTypes) {
        this.name = new Name(name);
        this.colorHex = new ColorHex(colorHex);
        this.icon = iconUrl == null ? null : new Image(iconUrl);
        addToScope(problemTypes);
    }

    public void addToScope(ProblemType problemType) {
        this.scope = this.scope.withAdded(problemType);
    }

    public void addToScope(Set<ProblemType> problemTypes) {
        this.scope = this.scope.withAdded(problemTypes);
    }

    public boolean isResponsibleBy(Problem problem) {
        return this.scope.embrace(problem.getType());
    }

    public UUID getId() {
        return id;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public AffiliatedUser getCreator() {
        return creator;
    }

    public String getName() {
        return name.value();
    }

    public String getColorHex() {
        return colorHex.value();
    }

    public String getIconUrl() {
        return icon == null ? null : icon.url();
    }

    public Set<ProblemType> getScope() {
        return scope.problemTypes();
    }

    public Instant getCreatedAt() {
        return timestamps.getCreatedAt();
    }

    public Instant getUpdatedAt() {
        return timestamps.getUpdatedAt();
    }

}
