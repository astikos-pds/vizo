package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.PermissionPresetDTO;
import br.app.vizo.application.usecase.permission.*;
import br.app.vizo.application.usecase.permission.params.ExistsPermissionPresetParams;
import br.app.vizo.application.usecase.permission.request.MutatePermissionPresetRequestDTO;
import br.app.vizo.config.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/municipalities/{municipalityId}/permission-presets")
@RequiredArgsConstructor
public class PermissionPresetController {

    private final GetPermissionPresetsInMunicipalityUseCase getPermissionPresetsInMunicipalityUseCase;
    private final GetPermissionPresetInMunicipalityUseCase getPermissionPresetInMunicipalityUseCase;
    private final ExistsPermissionPresetByParamsUseCase existsPermissionPresetByParamsUseCase;
    private final CreatePermissionPresetUseCase createPermissionPresetUseCase;
    private final UpdatePermissionPresetUseCase updatePermissionPresetUseCase;
    private final DeletePermissionPresetUseCase deletePermissionPresetUseCase;

    @GetMapping
    public ResponseEntity<List<PermissionPresetDTO>> getPermissionPresetsInMunicipality(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId
    ) {
        List<PermissionPresetDTO> body = this.getPermissionPresetsInMunicipalityUseCase
                .execute(userDetails.getUser(), municipalityId);

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionPresetDTO> getPermissionPresetInMunicipality(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable(name = "id") UUID permissionPresetId
    ) {
        PermissionPresetDTO response = this.getPermissionPresetInMunicipalityUseCase
                .execute(userDetails.getUser(), municipalityId, permissionPresetId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsPermissionPresetByParams(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @RequestParam String name
    ) {
        boolean response = this.existsPermissionPresetByParamsUseCase
                .execute(userDetails.getUser(), municipalityId, new ExistsPermissionPresetParams(name));

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PermissionPresetDTO> createPermissionPreset(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @RequestBody MutatePermissionPresetRequestDTO body
    ) {
        PermissionPresetDTO response = this.createPermissionPresetUseCase
                .execute(userDetails.getUser(), municipalityId, body);

        URI uri = UriComponentsBuilder.fromPath("/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionPresetDTO> updatePermissionPreset(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable(name = "id") UUID permissionPresetId,
            @RequestBody MutatePermissionPresetRequestDTO body
    ) {
        PermissionPresetDTO response = this.updatePermissionPresetUseCase
                .execute(userDetails.getUser(), municipalityId, permissionPresetId, body);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermissionPreset(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID municipalityId,
            @PathVariable(name = "id") UUID permissionPresetId
    ) {
        this.deletePermissionPresetUseCase.execute(userDetails.getUser(), municipalityId, permissionPresetId);

        return ResponseEntity.ok().build();
    }
}
