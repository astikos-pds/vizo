package br.app.vizo.domain.user;

import br.app.vizo.domain.user.avatar.Avatar;
import br.app.vizo.util.DateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@AllArgsConstructor
public abstract class User {

    @Id
    protected UUID id;

    protected String document;

    @Column(unique = true)
    protected String email;

    protected String password;

    protected String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Avatar avatar;

    @Column(name = "created_at")
    protected Instant createdAt;

    @Column(name = "updated_at")
    protected Instant updatedAt;

    public User() {
        this("", "", "", "", null);
    }

    public User(String document, String email, String password, String name, Avatar avatar) {
        this(UUID.randomUUID(), document, email, password, name, avatar, DateUtil.now(), DateUtil.now());
    }

    public boolean isOfficial() {
        return this instanceof Official;
    }
}
