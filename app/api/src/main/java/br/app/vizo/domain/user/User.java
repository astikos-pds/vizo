package br.app.vizo.domain.user;

import br.app.vizo.util.DateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
public class User {

    @Id
    protected UUID id;

    protected String document;

    @Column(unique = true)
    protected String email;

    protected String password;

    protected String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Avatar avatar;

    private Double credibilityPoints;

    @Column(name = "created_at")
    protected Instant createdAt;

    @Column(name = "updated_at")
    protected Instant updatedAt;

    public User() {
        this("", "", "", "", null, 1.0);
    }

    public User(String document, String email, String password, String name, Avatar avatar, Double credibilityPoints) {
        this(UUID.randomUUID(), document, email, password, name, avatar, credibilityPoints, DateUtil.now(), DateUtil.now());
    }
}
