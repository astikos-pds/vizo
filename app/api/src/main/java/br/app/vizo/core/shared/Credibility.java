package br.app.vizo.core.shared;

import br.app.vizo.core.shared.exception.InvalidCredibilityException;

public class Credibility {

    private final Double points;

    public Credibility() {
        this(1.0);
    }

    public Credibility(Double points) {
        if (points <= 0.0) {
            throw new InvalidCredibilityException();
        }
        this.points = points;
    }

    public Credibility increase(Double delta) {
        return new Credibility(this.points + delta);
    }

    public Credibility decrease(Double delta) {
        return new Credibility(Math.max(this.points - delta, 0));
    }

    public static Credibility of(Double points) {
        return new Credibility(points);
    }
}
