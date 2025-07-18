package br.app.vizo.service;

import br.app.vizo.controller.filter.AffiliationRequestFilter;
import br.app.vizo.controller.request.UpdateAffiliationRequestDTO;
import br.app.vizo.controller.response.*;
import br.app.vizo.domain.affiliation.AffiliationRequest;
import br.app.vizo.domain.affiliation.AffiliationRequestStatus;
import br.app.vizo.domain.department.Department;
import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.problem.Problem;
import br.app.vizo.domain.user.Official;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.*;
import br.app.vizo.repository.*;
import br.app.vizo.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MunicipalityService {

    private final OfficialService officialService;
    private final MunicipalityRepository municipalityRepository;
    private final AffiliationRequestRepository affiliationRequestRepository;
    private final OfficialRepository officialRepository;
    private final DepartmentRepository departmentRepository;
    private final ProblemRepository problemRepository;
    private final MunicipalityMapper municipalityMapper;
    private final AffiliationRequestMapper affiliationRequestMapper;
    private final ProblemMapper problemMapper;

    public MunicipalityService(
            OfficialService officialService,
            MunicipalityRepository municipalityRepository,
            AffiliationRequestRepository affiliationRequestRepository,
            OfficialRepository officialRepository,
            DepartmentRepository departmentRepository,
            ProblemRepository problemRepository,
            MunicipalityMapper municipalityMapper,
            AffiliationRequestMapper affiliationRequestMapper,
            ProblemMapper problemMapper
    ) {
        this.officialService = officialService;
        this.municipalityRepository = municipalityRepository;
        this.affiliationRequestRepository = affiliationRequestRepository;
        this.officialRepository = officialRepository;
        this.departmentRepository = departmentRepository;
        this.problemRepository = problemRepository;
        this.municipalityMapper = municipalityMapper;
        this.affiliationRequestMapper = affiliationRequestMapper;
        this.problemMapper = problemMapper;
    }

    public MunicipalityDTO getMunicipalityById(UUID id) {
        Municipality municipality = this.municipalityRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Municipality not found.")
        );

        return this.municipalityMapper.toDto(municipality);
    }

    public MunicipalityDTO getMunicipalityByEmailDomain(String domain) {
        Municipality municipality = this.municipalityRepository.findByEmailDomain(domain).orElseThrow(
                () -> new NotFoundException("Municipality not found.")
        );

        return this.municipalityMapper.toDto(municipality);
    }

    public Page<ProblemDTO> getDepartmentVisibleProblems(
            UUID municipalityId,
            UUID departmentId,
            Pageable pageable,
            Authentication authentication
    ) {
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

        Department department = this.departmentRepository.findById(departmentId).orElseThrow(
                () -> new NotFoundException("Department not found.")
        );

        return this.problemRepository.findAllByTypeIn(department.getProblemTypes(), pageable)
                .map(this.problemMapper::toDto);
    }

    public ProblemDTO getDepartmentProblemById(
            UUID municipalityId,
            UUID departmentId,
            UUID problemId,
            Authentication authentication
    ) {
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

        this.departmentRepository.findById(departmentId).orElseThrow(
                () -> new NotFoundException("Department not found.")
        );

        Problem problem = this.problemRepository.findById(problemId).orElseThrow(
                () -> new NotFoundException("Problem not found.")
        );

        return this.problemMapper.toDto(problem);
    }

    public Page<AffiliationRequestDTO> getMunicipalityAffiliations(
            UUID municipalityId,
            AffiliationRequestFilter filter,
            Pageable pageable,
            Authentication authentication
    ) {
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

        if (filter.status() == null) {
            return this.affiliationRequestRepository
                    .findAllByMunicipalityId(municipalityId, pageable)
                    .map(this.affiliationRequestMapper::toDto);
        }

        return this.affiliationRequestRepository
                .findAllByMunicipalityIdAndStatus(municipalityId, filter.status(), pageable)
                .map(this.affiliationRequestMapper::toDto);
    }

    public AffiliationRequestDTO updateMunicipalityAffiliation(
            UUID municipalityId,
            String affiliationId,
            UpdateAffiliationRequestDTO body,
            Authentication authentication
    ) {
        Official loggedInOfficial = this.officialService.getAuthorizedAdminContext(municipalityId, authentication).loggedInOfficial();

        AffiliationRequest affiliationRequest = this.affiliationRequestRepository
                .findById(UUID.fromString(affiliationId))
                .orElseThrow(() -> new NotFoundException("Affiliation request not found.")
        );

        affiliationRequest.setStatus(body.status());
        affiliationRequest.setApprovedBy(loggedInOfficial);
        affiliationRequest.setApprovedAt(DateUtil.now());

        Official official = affiliationRequest.getOfficial();
        official.setWasApproved(body.status() == AffiliationRequestStatus.APPROVED);
        this.officialRepository.save(official);

        return this.affiliationRequestMapper.toDto(this.affiliationRequestRepository.save(affiliationRequest));
    }
}
