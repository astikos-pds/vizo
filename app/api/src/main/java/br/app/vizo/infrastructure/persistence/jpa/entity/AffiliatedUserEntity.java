package br.app.vizo.infrastructure.persistence.jpa.entity;

import br.app.vizo.core.affiliation.AffiliationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "affiliated_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AffiliatedUserEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id")
    private MunicipalityEntity municipality;

    @Column(name = "institutional_email", nullable = false, unique = true)
    private String institutionalEmail;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @Enumerated(EnumType.STRING)
    private AffiliationStatus status;

    @Column(name = "affiliated_at", nullable = false)
    private Instant affiliatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private AffiliatedUserEntity approver;

    @Column(name = "approved_at")
    private Instant approvedAt;
}
