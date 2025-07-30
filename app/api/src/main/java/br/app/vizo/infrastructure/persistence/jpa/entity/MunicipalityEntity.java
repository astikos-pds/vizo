package br.app.vizo.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "municipalities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MunicipalityEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "email_domain", nullable = false, unique = true)
    private String emailDomain;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "created_at", nullable = false)
    protected Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    protected Instant updatedAt;

}
