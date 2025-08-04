package br.app.vizo.core.assignment;

import br.app.vizo.application.dto.page.PageDTO;
import br.app.vizo.application.dto.page.PaginationDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssignedUserRepository {

    AssignedUser save(AssignedUser assignedUser);

    List<AssignedUser> saveAll(List<AssignedUser> assignedUsers);

    Optional<AssignedUser> findById(UUID id);

    void deleteById(UUID id);

    Optional<AssignedUser> findByDepartmentIdAndAffiliatedUserId(UUID departmentId, UUID userId);

    PageDTO<AssignedUser> findAllByDepartmentId(UUID id, PaginationDTO pagination);

    PageDTO<AssignedUser> findAllByAffiliatedUserId(UUID userId, PaginationDTO pagination);

    void deleteAllByDepartmentId(UUID id);

}
