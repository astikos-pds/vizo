package br.app.vizo.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "citizens")
@Getter
@Setter
public class Citizen extends User {

    private String name;

    @Column(name = "credibility_points")
    private Double credibilityPoints;

    public Citizen() {
        super();
        this.name = "";
        this.credibilityPoints = 1.0;
    }

    public Citizen(String document, String email, String password, String name, Double credibilityPoints) {
        super(document, email, password);
        this.name = name;
        this.credibilityPoints = credibilityPoints;
    }

    public Citizen(String document, String email, String password, String name) {
        this(document, email, password, name, 1.0);
    }
}
