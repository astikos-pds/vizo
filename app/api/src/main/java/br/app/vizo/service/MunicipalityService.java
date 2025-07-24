package br.app.vizo.service;

import br.app.vizo.domain.department.Department;
import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.problem.Problem;
import br.app.vizo.dto.MunicipalityDTO;
import br.app.vizo.dto.ProblemDTO;
import br.app.vizo.exception.NotFoundException;
import br.app.vizo.mapper.*;
import br.app.vizo.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MunicipalityService {

    private final OfficialService officialService;
    private final MunicipalityRepository municipalityRepository;
    private final DepartmentRepository departmentRepository;
    private final ProblemRepository problemRepository;
    private final MunicipalityMapper municipalityMapper;
    private final ProblemMapper problemMapper;

    public MunicipalityService(
            OfficialService officialService,
            MunicipalityRepository municipalityRepository,
            DepartmentRepository departmentRepository,
            ProblemRepository problemRepository,
            MunicipalityMapper municipalityMapper,
            ProblemMapper problemMapper
    ) {
        this.officialService = officialService;
        this.municipalityRepository = municipalityRepository;
        this.departmentRepository = departmentRepository;
        this.problemRepository = problemRepository;
        this.municipalityMapper = municipalityMapper;
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
}
