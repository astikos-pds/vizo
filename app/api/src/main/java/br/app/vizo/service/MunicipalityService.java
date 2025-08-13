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
    private final OldMunicipalityRepository oldMunicipalityRepository;
    private final OldDepartmentRepository oldDepartmentRepository;
    private final OldProblemRepository oldProblemRepository;
    private final OldMunicipalityMapper oldMunicipalityMapper;
    private final OldProblemMapper oldProblemMapper;

    public MunicipalityService(
            OfficialService officialService,
            OldMunicipalityRepository oldMunicipalityRepository,
            OldDepartmentRepository oldDepartmentRepository,
            OldProblemRepository oldProblemRepository,
            OldMunicipalityMapper oldMunicipalityMapper,
            OldProblemMapper oldProblemMapper
    ) {
        this.officialService = officialService;
        this.oldMunicipalityRepository = oldMunicipalityRepository;
        this.oldDepartmentRepository = oldDepartmentRepository;
        this.oldProblemRepository = oldProblemRepository;
        this.oldMunicipalityMapper = oldMunicipalityMapper;
        this.oldProblemMapper = oldProblemMapper;
    }

    public MunicipalityDTO getMunicipalityById(UUID id) {
        Municipality municipality = this.oldMunicipalityRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Municipality not found.")
        );

        return this.oldMunicipalityMapper.toDto(municipality);
    }

    public MunicipalityDTO getMunicipalityByEmailDomain(String domain) {
        Municipality municipality = this.oldMunicipalityRepository.findByEmailDomain(domain).orElseThrow(
                () -> new NotFoundException("Municipality not found.")
        );

        return this.oldMunicipalityMapper.toDto(municipality);
    }

    public ProblemDTO getDepartmentProblemById(
            UUID municipalityId,
            UUID departmentId,
            UUID problemId,
            Authentication authentication
    ) {
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

        this.oldDepartmentRepository.findById(departmentId).orElseThrow(
                () -> new NotFoundException("Department not found.")
        );

        Problem problem = this.oldProblemRepository.findById(problemId).orElseThrow(
                () -> new NotFoundException("Problem not found.")
        );

        return this.oldProblemMapper.toDto(problem);
    }
}
