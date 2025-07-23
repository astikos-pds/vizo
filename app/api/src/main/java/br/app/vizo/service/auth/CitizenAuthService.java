package br.app.vizo.service.auth;

import br.app.vizo.controller.request.RegisterRequestDTO;
import br.app.vizo.dto.CitizenDTO;
import br.app.vizo.domain.user.Citizen;
import br.app.vizo.exception.UnauthorizedException;
import br.app.vizo.mapper.CitizenMapper;
import br.app.vizo.repository.CitizenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CitizenAuthService {

    private final CitizenRepository citizenRepository;
    private final CitizenMapper citizenMapper;
    private final PasswordEncoder passwordEncoder;

    public CitizenAuthService(
            CitizenRepository citizenRepository,
            CitizenMapper citizenMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.citizenRepository = citizenRepository;
        this.citizenMapper = citizenMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public CitizenDTO registerAsCitizen(RegisterRequestDTO body) {
        this.citizenRepository.findByDocumentOrEmail(body.document(), body.email()).ifPresent(
                c -> {
                    throw new UnauthorizedException("Invalid credentials.");
                }
        );

        Citizen citizen = new Citizen();
        citizen.setDocument(body.document());
        citizen.setEmail(body.email());
        citizen.setName(body.name());

        String hashedPassword = this.passwordEncoder.encode(body.password());
        citizen.setPassword(hashedPassword);

        return this.citizenMapper.toDto(this.citizenRepository.save(citizen));
    }
}
