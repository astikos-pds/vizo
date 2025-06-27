package br.app.vizo.domain.department;

import br.app.vizo.domain.user.Official;
import br.app.vizo.util.DateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@AllArgsConstructor
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "official_id")
    private Official official;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "role_in_department")
    @Enumerated(EnumType.STRING)
    private DepartmentRole roleInDepartment;

    @Column(name = "can_update_status")
    private Boolean canUpdateStatus;

    @Column(name = "can_view_reports")
    private Boolean canViewReports;

    @Column(name = "can_approve_officials")
    private Boolean canApproveOfficials;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    private Official addedBy;

    @Column(name = "added_at")
    private Instant addedAt;

    public Assignment() {
        this(0L, null, null, DepartmentRole.COMMON, false, true, false, null, DateUtil.now());
    }
}
