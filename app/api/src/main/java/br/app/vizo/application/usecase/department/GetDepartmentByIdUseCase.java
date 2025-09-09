package br.app.vizo.application.usecase.department;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.DepartmentDTO;
import br.app.vizo.application.exception.DepartmentNotFoundException;
import br.app.vizo.application.mapper.DepartmentMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.core.department.Department;
import br.app.vizo.core.department.DepartmentRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetDepartmentByIdUseCase {

    private final AuthorizationService authorizationService;
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentDTO execute(User loggedInUser, UUID municipalityId, UUID departmentId) {
        this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        Department department = this.departmentRepository.findById(departmentId)
                .orElseThrow(DepartmentNotFoundException::new);

        return this.departmentMapper.toDto(department);
    }
}
