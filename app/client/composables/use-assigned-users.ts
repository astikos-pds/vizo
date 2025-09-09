import { de } from "zod/v4/locales";
import type {
  AssignUsersToDepartmentRequest,
  AssignUserToDepartmentRequest,
  ChangeAssigneePermissionRequest,
} from "~/services/assigned-user";
import type { Department } from "~/types/domain";
import type { AssignedUser } from "~/types/domain/assigned-user";
import type { Municipality } from "~/types/domain/municipality";
import type { Pagination } from "~/types/domain/pagination";

export const useAssignedUsers = () => {
  const { $assignedUserService } = useNuxtApp();
  const { handle, loading } = useApiHandler();

  function getUsersAssignedToDepartment(
    municipalityId: Municipality["id"],
    departmentId: Department["id"],
    pagination: Pagination
  ) {
    return useAsyncData(
      `municipalities-${municipalityId}-departments-${departmentId}-assignments`,
      () =>
        $assignedUserService.getUsersAssignedToDepartment(
          municipalityId,
          departmentId,
          pagination
        )
    );
  }

  function getUserAssignedToDepartment(
    municipalityId: Municipality["id"],
    departmentId: Department["id"],
    assignmentId: AssignedUser["id"]
  ) {
    return useAsyncData(
      `municipalities-${municipalityId}-departments-${departmentId}-assignments-${assignmentId}`,
      () =>
        $assignedUserService.getUserAssignedToDepartment(
          municipalityId,
          departmentId,
          assignmentId
        )
    );
  }

  function assignUsersToDepartment(
    municipalityId: Municipality["id"],
    departmentId: Department["id"],
    request: AssignUsersToDepartmentRequest
  ) {
    return handle(() =>
      $assignedUserService.assignUsersToDepartment(
        municipalityId,
        departmentId,
        request
      )
    );
  }

  function assignUserToDepartment(
    municipalityId: Municipality["id"],
    departmentId: Department["id"],
    request: AssignUserToDepartmentRequest
  ) {
    return handle(() =>
      $assignedUserService.assignUserToDepartment(
        municipalityId,
        departmentId,
        request
      )
    );
  }

  function changeAssigneePermission(
    municipalityId: Municipality["id"],
    departmentId: Department["id"],
    assignmentId: AssignedUser["id"],
    request: ChangeAssigneePermissionRequest
  ) {
    return handle(() =>
      $assignedUserService.changeAssigneePermission(
        municipalityId,
        departmentId,
        assignmentId,
        request
      )
    );
  }

  function removeAssigneeFromDepartment(
    municipalityId: Municipality["id"],
    departmentId: Department["id"],
    assignmentId: AssignedUser["id"]
  ) {
    return handle(() =>
      $assignedUserService.removeAssigneeFromDepartment(
        municipalityId,
        departmentId,
        assignmentId
      )
    );
  }

  return {
    loading,
    getUsersAssignedToDepartment,
    getUserAssignedToDepartment,
    assignUsersToDepartment,
    assignUserToDepartment,
    changeAssigneePermission,
    removeAssigneeFromDepartment,
  };
};
