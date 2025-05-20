package br.app.vizo.service;

import br.app.vizo.config.security.JwtService;
import br.app.vizo.config.security.UserDetailsImpl;
import br.app.vizo.controller.response.TokenResponseDTO;
import br.app.vizo.domain.user.Citizen;
import br.app.vizo.domain.user.User;
import br.app.vizo.domain.user.dto.CitizenDTO;
import br.app.vizo.mapper.CitizenMapper;
import br.app.vizo.repository.CitizenRepository;
import br.app.vizo.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final CitizenRepository citizenRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final CitizenMapper citizenMapper;

    public AuthService(
            UserRepository userRepository,
            CitizenRepository citizenRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CitizenMapper citizenMapper
    ) {
        this.userRepository = userRepository;
        this.citizenRepository = citizenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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

    public TokenResponseDTO login(String document, String password) {
        User user = this.userRepository.findByDocument(document).orElseThrow(
                () -> new RuntimeException("Invalid credentials")
        );

        boolean passwordMatches = this.passwordEncoder.matches(password, user.getPassword());
        if (!passwordMatches) throw new RuntimeException("Invalid credentials");

        var authConfig = new UsernamePasswordAuthenticationToken(document, password);
        Authentication authentication = this.authenticationManager.authenticate(authConfig);
        UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token = this.jwtService.generateToken(userDetails);

        return new TokenResponseDTO(token);
    }
}
