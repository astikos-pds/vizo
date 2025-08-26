package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.PointOfInterestDTO;
import br.app.vizo.application.usecase.poi.CreatePointOfInterestUseCase;
import br.app.vizo.application.usecase.poi.DeletePointOfInterestUseCase;
import br.app.vizo.application.usecase.poi.GetPointOfInterestUseCase;
import br.app.vizo.application.usecase.poi.UpdatePointOfInterestUseCase;
import br.app.vizo.application.usecase.poi.request.MutatePointOfInterestRequestDTO;
import br.app.vizo.config.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(value = {"/points-of-interest", "/poi"})
@RequiredArgsConstructor
public class PointOfInterestController {

    private final GetPointOfInterestUseCase getPointOfInterestUseCase;
    private final CreatePointOfInterestUseCase createPointOfInterestUseCase;
    private final UpdatePointOfInterestUseCase updatePointOfInterestUseCase;
    private final DeletePointOfInterestUseCase deletePointOfInterestUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<PointOfInterestDTO> getPointOfInterest(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID id
    ) {
        PointOfInterestDTO response = this.getPointOfInterestUseCase.execute(userDetails.getUser(), id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PointOfInterestDTO> createPointOfInterest(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody MutatePointOfInterestRequestDTO body
    ) {
        PointOfInterestDTO response = this.createPointOfInterestUseCase.execute(userDetails.getUser(), body);

        URI uri = UriComponentsBuilder.fromPath("/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PointOfInterestDTO> updatePointOfInterest(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID id,
            @RequestBody MutatePointOfInterestRequestDTO body
    ) {
        PointOfInterestDTO response = this.updatePointOfInterestUseCase.execute(userDetails.getUser(), id, body);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePointOfInterest(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID id
    ) {
        this.deletePointOfInterestUseCase.execute(userDetails.getUser(), id);

        return ResponseEntity.ok().build();
    }
}
