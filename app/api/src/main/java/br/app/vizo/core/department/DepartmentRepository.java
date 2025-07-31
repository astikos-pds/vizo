package br.app.vizo.core.department;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository {

    Department save(Department department);

    Optional<Department> findById(UUID id);

    PageDTO<Department> findAllByMunicipalityId(UUID id, PaginationDTO pagination);
}
