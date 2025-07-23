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
@Table(name = "affiliation_requests")
@Getter
@Setter
@AllArgsConstructor
public class AffiliationRequest {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id")
    private Municipality municipality;

    @Enumerated(EnumType.STRING)
    private AffiliationRequestStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_by")
    private User approver;

    @Column(name = "approved_at")
    private Instant approvedAt;

    public AffiliationRequest() {
        this(UUID.randomUUID(), null, null, AffiliationRequestStatus.PENDING, DateUtil.now(), null, null);
    }
}
