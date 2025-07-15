package br.app.vizo.domain.user;

import br.app.vizo.domain.user.avatar.Avatar;
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
        this("", "", "", "", null, OfficialRole.OFFICIAL, false);
    }

    public Official(
            String document,
            String email,
            String password,
            String name,
            Avatar avatar,
            OfficialRole role,
            Boolean wasApproved
    ) {
        super(document, email, password, name, avatar);
        this.role = role;
        this.wasApproved = wasApproved;
    }

    public boolean isAdmin() {
        return this.role == OfficialRole.ADMIN;
    }
}
