package br.app.vizo.infrastructure.persistence.jpa.entity;

import br.app.vizo.core.problem.ProblemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id")
    private MunicipalityEntity municipality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private AffiliatedUserEntity creator;

    @Column(nullable = false)
    private String name;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "color_hex", nullable = false)
    private String colorHex;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "scopes", joinColumns = @JoinColumn(name = "department_id"))
    @Column(name = "type")
    private Set<ProblemType> scope = new HashSet<>();

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
