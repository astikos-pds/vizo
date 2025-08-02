package br.app.vizo.application.usecase.department;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.DepartmentDTO;
import br.app.vizo.application.mapper.DepartmentMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.application.usecase.department.request.CreateDepartmentRequestDTO;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.department.Department;
import br.app.vizo.core.department.DepartmentRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class CreateDepartmentUseCase {

    private final AuthorizationService authorizationService;
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentDTO execute(User loggedInUser, UUID municipalityId, CreateDepartmentRequestDTO request) {
        AffiliatedUser affiliatedUser =  this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        Department department = affiliatedUser.createDepartment(
                request.name(),
                request.colorHex(),
                request.iconUrl(),
                request.problemTypes()
        );

        Department saved = this.departmentRepository.save(department);
        return this.departmentMapper.toDto(saved);
    }
}
