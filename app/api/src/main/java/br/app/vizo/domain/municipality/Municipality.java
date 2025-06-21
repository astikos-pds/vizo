package br.app.vizo.domain.municipality;

import br.app.vizo.util.DateUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "municipalities")
@Getter
@Setter
@AllArgsConstructor
public class Municipality {

    @Id
    private UUID id;

    private String name;

    @Column(name = "email_domain", unique = true)
    private String emailDomain;

    @Column(name = "created_at")
    protected Instant createdAt;

    @Column(name = "updated_at")
    protected Instant updatedAt;

    public Municipality() {
        this("", "");
    }

    public Municipality(String name, String emailDomain) {
        this(UUID.randomUUID(), name, emailDomain, DateUtil.now(), DateUtil.now());
    }
}
