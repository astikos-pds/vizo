package br.app.vizo.domain.department;

import br.app.vizo.domain.user.User;
import br.app.vizo.util.DateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@AllArgsConstructor
public class Assignment {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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
    @JoinColumn(name = "assignor_id")
    private User assignor;

    @Column(name = "created_at")
    private Instant createdAt;

    public Assignment() {
        this(UUID.randomUUID(), null, null, DepartmentRole.COMMON, false, true, false, null, DateUtil.now());
    }
}
