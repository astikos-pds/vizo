package br.app.vizo.service;

import br.app.vizo.controller.request.UpdateAffiliationRequestDTO;
import br.app.vizo.controller.request.CreateDepartmentRequestDTO;
import br.app.vizo.controller.response.AffiliationRequestDTO;
import br.app.vizo.controller.response.DepartmentDTO;
import br.app.vizo.domain.affiliation.AffiliationRequest;
import br.app.vizo.domain.affiliation.AffiliationRequestStatus;
import br.app.vizo.domain.department.Department;
import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.user.Official;
import br.app.vizo.domain.user.OfficialRole;
import br.app.vizo.exception.http.ForbiddenException;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.AffiliationRequestMapper;
import br.app.vizo.mapper.DepartmentMapper;
import br.app.vizo.repository.AffiliationRequestRepository;
import br.app.vizo.repository.DepartmentRepository;
import br.app.vizo.repository.MunicipalityRepository;
import br.app.vizo.repository.OfficialRepository;
import br.app.vizo.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MunicipalityService {

    private final MunicipalityRepository municipalityRepository;
    private final AffiliationRequestRepository affiliationRequestRepository;
    private final OfficialRepository officialRepository;
    private final DepartmentRepository departmentRepository;
    private final AffiliationRequestMapper affiliationRequestMapper;
    private final DepartmentMapper departmentMapper;

    public MunicipalityService(
            MunicipalityRepository municipalityRepository,
            AffiliationRequestRepository affiliationRequestRepository,
            OfficialRepository officialRepository,
            DepartmentRepository departmentRepository,
            AffiliationRequestMapper affiliationRequestMapper,
            DepartmentMapper departmentMapper
    ) {
        this.municipalityRepository = municipalityRepository;
        this.affiliationRequestRepository = affiliationRequestRepository;
        this.officialRepository = officialRepository;
        this.departmentRepository = departmentRepository;
        this.affiliationRequestMapper = affiliationRequestMapper;
        this.departmentMapper = departmentMapper;
    }

    public DepartmentDTO createMunicipalityDepartment(
            UUID municipalityId,
            CreateDepartmentRequestDTO body,
            Authentication authentication
    ) {

        OfficialContext officialContext = this.checkIfOfficialIsAuthorized(municipalityId, authentication.getName());

        Department department = new Department();
        department.setName(body.name());
        department.setIconUrl(body.iconUrl());
        department.setColorHex(body.colorHex());
        department.setCreatedBy(officialContext.loggedInOfficial());
        department.setMunicipality(officialContext.municipality());

        return this.departmentMapper.toDto(this.departmentRepository.save(department));
    }

    public Page<AffiliationRequestDTO> getMunicipalityAffiliations(
            UUID municipalityId,
            Pageable pageable,
            Authentication authentication
    ) {
        this.checkIfOfficialIsAuthorized(municipalityId, authentication.getName());

        return this.affiliationRequestRepository
                .findAllByMunicipalityId(municipalityId, pageable)
                .map(this.affiliationRequestMapper::toDto);
    }

    public AffiliationRequestDTO updateMunicipalityAffiliation(
            UUID municipalityId,
            String affiliationId,
            UpdateAffiliationRequestDTO body,
            Authentication authentication
    ) {
        Official loggedInOfficial = this.checkIfOfficialIsAuthorized(municipalityId, authentication.getName())
                .loggedInOfficial();

        AffiliationRequest affiliationRequest = this.affiliationRequestRepository
                .findById(UUID.fromString(affiliationId))
                .orElseThrow(() -> new NotFoundException("Affiliation request not found.")
        );

        affiliationRequest.setStatus(body.status());
        affiliationRequest.setApprovedBy(loggedInOfficial);
        affiliationRequest.setApprovedAt(DateUtil.now());

        if (body.status() == AffiliationRequestStatus.APPROVED) {
            Official official = affiliationRequest.getOfficial();
            official.setWasApproved(true);
            this.officialRepository.save(official);
        }

        return this.affiliationRequestMapper.toDto(this.affiliationRequestRepository.save(affiliationRequest));
    }

    private record OfficialContext(
            Municipality municipality,
            Official loggedInOfficial
    ) {}

    private OfficialContext checkIfOfficialIsAuthorized(UUID municipalityId, String officialDocument) {
        Municipality municipality = this.municipalityRepository.findById(municipalityId).orElseThrow(
                () -> new NotFoundException("Municipality not found.")
        );

        Official loggedInOfficial = this.officialRepository.findByDocument(officialDocument).orElseThrow(
                () -> new NotFoundException("Official not found.")
        );

        if (!loggedInOfficial.getMunicipality().getId().equals(municipalityId)
                || loggedInOfficial.getRole() != OfficialRole.ADMIN
        ) {
            throw new ForbiddenException("Permission denied to update affiliation request.");
        }

        return new OfficialContext(municipality, loggedInOfficial);
    }
}
