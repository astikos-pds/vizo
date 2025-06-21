package br.app.vizo.domain.affiliation;

import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.user.Official;
import br.app.vizo.util.DateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "affiliation_requests")
@Getter
@Setter
@AllArgsConstructor
public class AffiliationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "official_id")
    private Official official;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id")
    private Municipality municipality;

    @Enumerated(EnumType.STRING)
    private AffiliationRequestStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Official approvedBy;

    @Column(name = "approved_at")
    private Instant approvedAt;

    public AffiliationRequest() {
        this(0L, null, null, AffiliationRequestStatus.PENDING, DateUtil.now(), null, null);
    }
}
