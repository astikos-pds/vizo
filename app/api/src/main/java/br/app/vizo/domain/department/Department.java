package br.app.vizo.domain.department;

import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.problem.ProblemType;
import br.app.vizo.domain.user.Official;
import br.app.vizo.util.DateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "departments")
@Getter
@Setter
@AllArgsConstructor
public class Department {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id")
    private Municipality municipality;

    private String name;

    private String iconUrl;

    @Column(name = "color_hex")
    private String colorHex;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "department_problem_type_permission", joinColumns = @JoinColumn(name = "department_id"))
    @Column(name = "type")
    private Set<ProblemType> problemTypes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Official createdBy;

    @Column(name = "created_at")
    protected Instant createdAt;

    @Column(name = "updated_at")
    protected Instant updatedAt;

    public Department() {
        this(null, "", "", "", Set.of(), null);
    }

    public Department(
            Municipality municipality,
            String name,
            String iconUrl,
            String colorHex,
            Set<ProblemType> problemTypes,
            Official createdBy
    ) {
        this(UUID.randomUUID(), municipality, name, iconUrl, colorHex, problemTypes, createdBy, DateUtil.now(), DateUtil.now());
    }
}
