package br.app.vizo.domain.affiliation;

import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.user.User;
import br.app.vizo.util.DateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "old_affiliations")
@Getter
@Setter
@AllArgsConstructor
public class Affiliation {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id")
    private Municipality municipality;

    @Column(name = "institutional_email", unique = true, nullable = false)
    private String institutionalEmail;

    @Enumerated(EnumType.STRING)
    private AffiliationStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private User approver;

    @Column(name = "approved_at")
    private Instant approvedAt;

    public Affiliation() {
        this(UUID.randomUUID(), null, null, "", AffiliationStatus.PENDING, DateUtil.now(), null, null);
    }
}
