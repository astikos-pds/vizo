package br.app.vizo.application.usecase.permission.params;

import br.app.vizo.core.shared.Name;

public record GetPermissionPresetParams(
        Name name
) {

    public GetPermissionPresetParams(String name) {
        this(new Name(name));
    }

    public String getName() {
        return name.value();
    }
}
