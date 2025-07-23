package br.app.vizo.service.auth;

import br.app.vizo.controller.request.RegisterRequestDTO;
import br.app.vizo.dto.OfficialDTO;
import br.app.vizo.domain.affiliation.AffiliationRequest;
import br.app.vizo.domain.affiliation.AffiliationRequestStatus;
import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.user.Official;
import br.app.vizo.domain.user.OfficialRole;
import br.app.vizo.domain.verification.EmailVerificationRequest;
import br.app.vizo.exception.UnauthorizedException;
import br.app.vizo.mapper.OfficialMapper;
import br.app.vizo.repository.AffiliationRequestRepository;
import br.app.vizo.repository.EmailVerificationRequestRepository;
import br.app.vizo.repository.MunicipalityRepository;
import br.app.vizo.repository.OfficialRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OfficialAuthService {

    private final OfficialRepository officialRepository;
    private final OfficialMapper officialMapper;
    private final MunicipalityRepository municipalityRepository;
    private final AffiliationRequestRepository affiliationRequestRepository;
    private final EmailVerificationRequestRepository emailVerificationRequestRepository;
    private final PasswordEncoder passwordEncoder;

    public OfficialAuthService(
            OfficialRepository officialRepository,
            OfficialMapper officialMapper,
            MunicipalityRepository municipalityRepository,
            AffiliationRequestRepository affiliationRequestRepository,
            EmailVerificationRequestRepository emailVerificationRequestRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.officialRepository = officialRepository;
        this.officialMapper = officialMapper;
        this.municipalityRepository = municipalityRepository;
        this.affiliationRequestRepository = affiliationRequestRepository;
        this.emailVerificationRequestRepository = emailVerificationRequestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public OfficialDTO registerAsOfficial(RegisterRequestDTO body) {
        Optional<EmailVerificationRequest> emailVerificationRequest = this.emailVerificationRequestRepository
                .findByEmail(body.email());

        if (emailVerificationRequest.isEmpty() || !emailVerificationRequest.get().isVerified()) {
            throw new UnauthorizedException("Invalid credentials.");
        }

        this.officialRepository.findByDocumentOrEmail(body.document(), body.email()).ifPresent(
                o -> {
                    throw new UnauthorizedException("Invalid credentials.");
                }
        );

        String emailDomain = body.email().split("@")[1];
        Municipality municipality = this.municipalityRepository.findByEmailDomain(emailDomain).orElseThrow(
                () -> new UnauthorizedException("Unknown municipality's e-mail domain.")
        );

        Official official = new Official();
        official.setDocument(body.document());
        official.setEmail(body.email());
        official.setName(body.name());
        official.setRole(OfficialRole.OFFICIAL);
        official.setWasApproved(false);

        String hashedPassword = this.passwordEncoder.encode(body.password());
        official.setPassword(hashedPassword);

        official = this.officialRepository.save(official);

        AffiliationRequest affiliationRequest = new AffiliationRequest();
        affiliationRequest.setOfficial(official);
        affiliationRequest.setMunicipality(municipality);
        affiliationRequest.setStatus(AffiliationRequestStatus.PENDING);

        this.affiliationRequestRepository.save(affiliationRequest);

        return this.officialMapper.toDto(official);
    }

}
