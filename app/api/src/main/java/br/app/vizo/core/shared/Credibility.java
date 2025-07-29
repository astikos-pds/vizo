package br.app.vizo.core.shared;

import br.app.vizo.core.shared.exception.InvalidCredibilityException;

public record Credibility(
        Double points
) {
    public Credibility {
        if (points <= 0.0) {
            throw new InvalidCredibilityException();
        }
    }

    public Credibility() {
        this(1.0);
    }

    public Credibility accumulate(Double delta) {
        return new Credibility(this.points + delta);
    }

    public static Credibility of(Double points) {
        return new Credibility(points);
    }
}
