package br.app.vizo.service;

import br.app.vizo.domain.user.Citizen;
import br.app.vizo.domain.user.dto.CitizenDTO;
import br.app.vizo.mapper.CitizenMapper;
import br.app.vizo.repository.CitizenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final CitizenRepository citizenRepository;

    private final PasswordEncoder passwordEncoder;

    private final CitizenMapper citizenMapper;

    public AuthService(
            CitizenRepository citizenRepository,
            PasswordEncoder passwordEncoder,
            CitizenMapper citizenMapper
    ) {
        this.citizenRepository = citizenRepository;
        this.passwordEncoder = passwordEncoder;
        this.citizenMapper = citizenMapper;
    }

    public CitizenDTO registerCitizen(String document, String email, String password, String name) {
        Optional<Citizen> existingCitizen = this.citizenRepository.findByDocument(document);

        if (existingCitizen.isPresent()) throw new RuntimeException("Invalid credentials.");

        Citizen citizen = new Citizen();
        citizen.setDocument(document);
        citizen.setEmail(email);
        citizen.setName(name);

        String hashedPassword = this.passwordEncoder.encode(password);
        citizen.setPassword(hashedPassword);

        return this.citizenMapper.toDto(this.citizenRepository.save(citizen));
    }
}
