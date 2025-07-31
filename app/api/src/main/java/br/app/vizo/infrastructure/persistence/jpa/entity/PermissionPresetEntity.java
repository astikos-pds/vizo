package br.app.vizo.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "permission_presets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionPresetEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id")
    private MunicipalityEntity municipality;

    @Column(nullable = false)
    private String name;

    @Embedded
    private PermissionEntity permission;

}
