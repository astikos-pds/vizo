package br.app.vizo.core.assignment;

import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.shared.Name;

import java.util.UUID;

public class PermissionPreset {

    private final UUID id;
    private final Municipality municipality;
    private Name name;
    private Permission permission;

    public PermissionPreset(UUID id, Municipality municipality, Name name, Permission permission) {
        this.id = id;
        this.municipality = municipality;
        this.name = name;
        this.permission = permission;
    }

    public PermissionPreset(Municipality municipality, String name, Permission permission) {
        this(UUID.randomUUID(), municipality, new Name(name), permission);
    }

    public void updateName(String name) {
        this.name = new Name(name);
    }

    public void updatePermission(Permission permission) {
        this.permission = permission;
    }

    public UUID getId() {
        return id;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public String getName() {
        return name.value();
    }

    public Permission getPermission() {
        return permission;
    }
}
