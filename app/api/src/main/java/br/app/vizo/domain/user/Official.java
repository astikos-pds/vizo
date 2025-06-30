package br.app.vizo.domain.user;

import br.app.vizo.domain.municipality.Municipality;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "officials")
@Getter
@Setter
public class Official extends User {

    @Enumerated(EnumType.STRING)
    private OfficialRole role;

    @Column(name = "was_approved")
    private Boolean wasApproved;

    public Official() {
        this("", "", "", "", OfficialRole.OFFICIAL, false);
    }

    public Official(
            String document,
            String email,
            String password,
            String name,
            OfficialRole role,
            Boolean wasApproved
    ) {
        super(document, email, password, name);
        this.role = role;
        this.wasApproved = wasApproved;
    }

    public boolean isAdmin() {
        return this.role == OfficialRole.ADMIN;
    }
}
