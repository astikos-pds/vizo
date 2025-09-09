package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;
import br.app.vizo.application.mapper.AssignedUserMapper;
import br.app.vizo.core.assignment.AssignedUser;
import br.app.vizo.core.assignment.AssignedUserRepository;
import br.app.vizo.infrastructure.persistence.jpa.entity.AssignedUserEntity;
import br.app.vizo.infrastructure.persistence.jpa.repository.AssignedUserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AssignedUserRepositoryImpl implements AssignedUserRepository {

    private final AssignedUserJpaRepository jpaRepository;
    private final AssignedUserMapper mapper;

    public AssignedUserRepositoryImpl(AssignedUserJpaRepository jpaRepository, AssignedUserMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public AssignedUser save(AssignedUser assignedUser) {
        AssignedUserEntity entity = this.mapper.toEntity(assignedUser);
        AssignedUserEntity saved = this.jpaRepository.save(entity);
        return this.mapper.toModel(saved);
    }

    @Override
    public List<AssignedUser> saveAll(List<AssignedUser> assignedUsers) {
        List<AssignedUserEntity> entities = assignedUsers.stream().map(this.mapper::toEntity).toList();
        List<AssignedUserEntity> saved = this.jpaRepository.saveAll(entities);
        return saved.stream().map(this.mapper::toModel).toList();
    }

    @Override
    public Optional<AssignedUser> findById(UUID id) {
        return this.jpaRepository.findById(id).map(this.mapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {
        this.jpaRepository.deleteById(id);
    }

    @Override
    public Optional<AssignedUser> findByDepartmentIdAndAffiliatedUserId(UUID departmentId, UUID userId) {
        return this.jpaRepository.findByDepartmentIdAndAffiliatedUserId(departmentId, userId).map(this.mapper::toModel);
    }

    @Override
    public PageDTO<AssignedUser> findAllByDepartmentId(UUID id, PaginationDTO pagination) {
        var page = this.jpaRepository.findAllByDepartmentId(id, PaginationDTO.resolve(pagination))
                .map(this.mapper::toModel);
        return PageDTO.of(page);
    }

    @Override
    public PageDTO<AssignedUser> findAllByAffiliatedUserId(UUID userId, PaginationDTO pagination) {
        var page = this.jpaRepository.findAllByAffiliatedUserId(userId, PaginationDTO.resolve(pagination))
                .map(this.mapper::toModel);
        return PageDTO.of(page);
    }

    @Override
    public void deleteAllByDepartmentId(UUID id) {
        this.jpaRepository.deleteAllByDepartmentId(id);
    }
}
