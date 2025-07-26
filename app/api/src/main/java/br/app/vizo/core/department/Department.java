package br.app.vizo.core.department;

import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.department.exception.InvalidDepartmentException;
import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.problem.Problem;
import br.app.vizo.core.problem.ProblemType;
import br.app.vizo.core.shared.Image;
import br.app.vizo.core.shared.Name;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

public class Department {

    @Getter private final UUID id;
    @Getter private final Municipality municipality;
    @Getter private final AffiliatedUser creator;
    private Name name;
    private ColorHex colorHex;
    private Image icon;
    private ProblemScope scope;

    public Department(
            Municipality municipality,
            AffiliatedUser creator,
            String name,
            String colorHex,
            String iconUrl,
            Set<ProblemType> problemTypes
    ) {
        if (municipality == null || creator == null) {
            throw new InvalidDepartmentException();
        }

        this.id = UUID.randomUUID();
        this.municipality = municipality;
        this.creator = creator;
        this.name = new Name(name);
        this.colorHex = new ColorHex(colorHex);
        this.icon = new Image(iconUrl);
        this.scope = new ProblemScope(problemTypes);
    }

    public void update(String name, String colorHex, String iconUrl, Set<ProblemType> problemTypes) {
        this.name = new Name(name);
        this.colorHex = new ColorHex(colorHex);
        this.icon = new Image(iconUrl);
        addToScope(problemTypes);
    }

    public void addToScope(ProblemType problemType) {
        this.scope = this.scope.withAdded(problemType);
    }

    public void addToScope(Set<ProblemType> problemTypes) {
        this.scope = this.scope.withAdded(problemTypes);
    }

    public String getName() {
        return name.value();
    }

    public String getColorHex() {
        return colorHex.value();
    }

    public String getIconUrl() {
        return icon.url();
    }

    public boolean isResponsibleBy(Problem problem) {
        return this.scope.embrace(problem.getType());
    }

}
