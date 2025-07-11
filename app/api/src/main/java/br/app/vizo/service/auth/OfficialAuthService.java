package br.app.vizo.service.auth;

import br.app.vizo.controller.request.RegisterRequestDTO;
import br.app.vizo.controller.response.MunicipalityDTO;
import br.app.vizo.controller.response.OfficialDTO;
import br.app.vizo.domain.affiliation.AffiliationRequest;
import br.app.vizo.domain.affiliation.AffiliationRequestStatus;
import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.user.Official;
import br.app.vizo.domain.user.OfficialRole;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.exception.http.UnauthorizedException;
import br.app.vizo.mapper.MunicipalityMapper;
import br.app.vizo.mapper.OfficialMapper;
import br.app.vizo.repository.AffiliationRequestRepository;
import br.app.vizo.repository.MunicipalityRepository;
import br.app.vizo.repository.OfficialRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class OfficialAuthService {

    private final OfficialRepository officialRepository;
    private final OfficialMapper officialMapper;
    private final MunicipalityRepository municipalityRepository;
    private final MunicipalityMapper municipalityMapper;
    private final AffiliationRequestRepository affiliationRequestRepository;
    private final PasswordEncoder passwordEncoder;

    public OfficialAuthService(
            OfficialRepository officialRepository,
            OfficialMapper officialMapper,
            MunicipalityRepository municipalityRepository,
            MunicipalityMapper municipalityMapper,
            AffiliationRequestRepository affiliationRequestRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.officialRepository = officialRepository;
        this.officialMapper = officialMapper;
        this.municipalityRepository = municipalityRepository;
        this.municipalityMapper = municipalityMapper;
        this.affiliationRequestRepository = affiliationRequestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public OfficialDTO registerAsOfficial(RegisterRequestDTO body) {
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

    public MunicipalityDTO getMunicipalityByEmailDomain(String domain) {
        Municipality municipality = this.municipalityRepository.findByEmailDomain(domain).orElseThrow(
                () -> new NotFoundException("Municipality not found.")
        );

        return this.municipalityMapper.toDto(municipality);
    }
}
