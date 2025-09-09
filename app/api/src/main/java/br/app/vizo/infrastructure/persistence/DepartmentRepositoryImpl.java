package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.DepartmentMapper;
import br.app.vizo.core.department.Department;
import br.app.vizo.core.department.DepartmentRepository;
import br.app.vizo.infrastructure.persistence.jpa.entity.DepartmentEntity;
import br.app.vizo.infrastructure.persistence.jpa.repository.DepartmentJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentJpaRepository jpaRepository;
    private final DepartmentMapper mapper;

    public DepartmentRepositoryImpl(DepartmentJpaRepository jpaRepository, DepartmentMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Department save(Department department) {
        DepartmentEntity entity = this.mapper.toEntity(department);
        DepartmentEntity saved = this.jpaRepository.save(entity);
        return this.mapper.toModel(saved);
    }

    @Override
    public Optional<Department> findById(UUID id) {
        return this.jpaRepository.findById(id).map(this.mapper::toModel);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.jpaRepository.existsById(id);
    }

    @Override
    public PageDTO<Department> findAllByMunicipalityId(UUID id, PaginationDTO pagination) {
        var page = this.jpaRepository.findAllByMunicipalityId(id, PaginationDTO.resolve(pagination))
                .map(this.mapper::toModel);
        return PageDTO.of(page);
    }

    @Override
    public void deleteById(UUID id) {
        this.jpaRepository.deleteById(id);
    }
}
