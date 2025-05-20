package br.app.vizo.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "municipalities")
@Getter
@Setter
public class Municipality extends User {

    @Column(name = "city_name")
    private String cityName;

    public Municipality() {
        super();
        this.cityName = "";
    }

    public Municipality(String document, String email, String password, String cityName) {
        super(document, email, password);
        this.cityName = cityName;
    }
}
