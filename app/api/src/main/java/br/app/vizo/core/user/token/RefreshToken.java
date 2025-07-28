package br.app.vizo.core.user.token;

import br.app.vizo.core.shared.ExpirationTimestamp;
import br.app.vizo.core.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String token;

    @Embedded
    private ExpirationTimestamp expiresAt;

    @Column(name = "created_at", nullable = false)
    private  Instant createdAt;

    RefreshToken() {
    }

    public RefreshToken(User user, String token, ExpirationTimestamp expiresAt) {
        this.id = 1L;
        this.user = user;
        this.token = token;
        this.expiresAt = expiresAt;
        this.createdAt = Instant.now();
    }

    public boolean isExpired() {
        return this.expiresAt.isExpired();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public Instant getExpiresAt() {
        return expiresAt.value();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
