package br.app.vizo.core.assignment;

import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.shared.Name;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PermissionPreset {

    @Getter private final UUID id;
    @Getter private final Municipality municipality;
    private Name name;
    @Getter private Permission permission;

    public PermissionPreset(Municipality municipality, String name, Permission permission) {
        this(UUID.randomUUID(), municipality, new Name(name), permission);
    }

    public String getName() {
        return name.value();
    }
}
