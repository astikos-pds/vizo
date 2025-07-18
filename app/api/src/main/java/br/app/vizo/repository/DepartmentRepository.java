package br.app.vizo.repository;

import br.app.vizo.domain.department.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    Page<Department> findAllByMunicipalityId(UUID municipalityId, Pageable pageable);
}
