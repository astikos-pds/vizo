package br.app.vizo.core.department;

import br.app.vizo.core.department.exception.InvalidProblemScopeException;
import br.app.vizo.core.problem.ProblemType;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public record ProblemScope(Set<ProblemType> problemTypes) {

    public ProblemScope {
        if (problemTypes == null || problemTypes.isEmpty()) {
            throw new InvalidProblemScopeException();
        }
    }

    public ProblemScope withAdded(ProblemType type) {
        Set<ProblemType> updated = new HashSet<>(this.problemTypes);
        updated.add(type);
        return new ProblemScope(updated);
    }

    public ProblemScope withAdded(Set<ProblemType> types) {
        Set<ProblemType> updated = new HashSet<>(this.problemTypes);
        updated.addAll(types);
        return new ProblemScope(updated);
    }

    public boolean embrace(ProblemType problemType) {
        return problemTypes.contains(problemType);
    }
}
