package br.app.vizo.application.usecase.department;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.DepartmentDTO;
import br.app.vizo.application.exception.DepartmentNotFoundException;
import br.app.vizo.application.mapper.DepartmentMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.application.usecase.department.request.MutateDepartmentRequestDTO;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.department.Department;
import br.app.vizo.core.department.DepartmentRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class UpdateDepartmentUseCase {

    private final AuthorizationService authorizationService;
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentDTO execute(User loggedInUser, UUID municipalityId, UUID departmentId, MutateDepartmentRequestDTO request) {
        AffiliatedUser affiliatedUser = this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        Department department = this.departmentRepository.findById(departmentId)
                .orElseThrow(DepartmentNotFoundException::new);

        Department updated = affiliatedUser.updateDepartment(
                department,
                request.name(),
                request.colorHex(),
                request.iconUrl(),
                request.problemTypes()
        );

        this.departmentRepository.save(updated);

        return this.departmentMapper.toDto(updated);
    }
}
