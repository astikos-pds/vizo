package br.app.vizo.domain.user;

import br.app.vizo.domain.user.avatar.Avatar;
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

    @Column(name = "credibility_points")
    private Double credibilityPoints;

    public Citizen() {
        this("", "", "", "", null, 1.0);
    }

    public Citizen(String document, String email, String password, String name, Avatar avatar, Double credibilityPoints) {
        super(document, email, password, name, avatar);
        this.credibilityPoints = credibilityPoints;
    }
}
