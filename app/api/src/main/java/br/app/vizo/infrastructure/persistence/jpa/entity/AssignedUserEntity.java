package br.app.vizo.infrastructure.persistence.jpa.entity;

import br.app.vizo.core.assignment.permission.PermissionMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assigned_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignedUserEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliated_user_id")
    private AffiliatedUserEntity affiliatedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_mode", nullable = false)
    private PermissionMode permissionMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_preset_id")
    private PermissionPresetEntity permissionPreset;

    @Embedded
    private PermissionEntity customPermission;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;
}
