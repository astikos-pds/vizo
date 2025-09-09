package br.app.vizo.infrastructure.web;

import br.app.vizo.application.dto.MunicipalityDTO;
import br.app.vizo.application.usecase.municipality.GetMunicipalityByEmailDomainUseCase;
import br.app.vizo.application.usecase.municipality.GetMunicipalityByIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/municipalities")
@RequiredArgsConstructor
public class MunicipalityController {

    private final GetMunicipalityByIdUseCase getMunicipalityByIdUseCase;
    private final GetMunicipalityByEmailDomainUseCase getMunicipalityByEmailDomainUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<MunicipalityDTO> getMunicipalityById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.getMunicipalityByIdUseCase.execute(id));
    }

    @GetMapping
    public ResponseEntity<MunicipalityDTO> getMunicipalityByEmailDomain(
            @RequestParam(name = "domain") String emailDomain
    ) {
        return ResponseEntity.ok(this.getMunicipalityByEmailDomainUseCase.execute(emailDomain));
    }
}
