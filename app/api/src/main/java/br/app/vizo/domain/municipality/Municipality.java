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
@Table(name = "old_municipalities")
@Getter
@Setter
@AllArgsConstructor
public class Municipality {

    @Id
    private UUID id;

    private String name;

    @Column(name = "email_domain", unique = true)
    private String emailDomain;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "created_at")
    protected Instant createdAt;

    @Column(name = "updated_at")
    protected Instant updatedAt;

    public Municipality() {
        this("", "", "");
    }

    public Municipality(String name, String emailDomain, String iconUrl) {
        this(UUID.randomUUID(), name, emailDomain, iconUrl, DateUtil.now(), DateUtil.now());
    }
}
