package br.app.vizo.domain.user;

import br.app.vizo.util.DateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "OldUser")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
public class User {

    @Id
    private UUID id;

    private String document;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Avatar avatar;

    private Double credibilityPoints;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public User() {
        this("", "", "", "", null, 1.0);
    }

    public User(String document, String email, String password, String name, Avatar avatar, Double credibilityPoints) {
        this(UUID.randomUUID(), document, email, password, name, avatar, credibilityPoints, DateUtil.now(), DateUtil.now());
    }
}
