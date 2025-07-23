package br.app.vizo.repository;

import br.app.vizo.domain.department.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

    Optional<Assignment> findByDepartmentIdAndUserId(UUID departmentId, UUID userId);

    Page<Assignment> findAllByDepartmentId(UUID departmentId, Pageable pageable);

    List<Assignment> findAllByDepartmentMunicipalityIdAndUserId(UUID municipalityId, UUID userId);

    void deleteAllByDepartmentId(UUID departmentId);
}
