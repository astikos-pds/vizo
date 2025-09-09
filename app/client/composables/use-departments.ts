import type {
  ChangeProblemStatusInScopeRequest,
  MutateDepartmentRequest,
} from "~/services/department";
import type { Department } from "~/types/domain/department";
import type { Municipality } from "~/types/domain/municipality";
import type { Pagination } from "~/types/domain/pagination";
import type { Problem } from "~/types/domain/problem";

export const useDepartments = () => {
  const { $departmentService } = useNuxtApp();

  const { handle, loading } = useApiHandler();

  function getDepartmentById(
    municipalityId: Municipality["id"],
    departmentId: Department["id"]
  ) {
    return useAsyncData(
      `municipalities-${municipalityId}-departments-${departmentId}`,
      () => $departmentService.getDepartmentById(municipalityId, departmentId)
    );
  }

  function createDepartment(
    municipalityId: Municipality["id"],
    request: MutateDepartmentRequest
  ) {
    return handle(() =>
      $departmentService.createDepartment(municipalityId, request)
    );
  }

  function updateDepartment(
    municipalityId: Municipality["id"],
    departmentId: Department["id"],
    request: MutateDepartmentRequest
  ) {
    return handle(() =>
      $departmentService.updateDepartment(municipalityId, departmentId, request)
    );
  }

  function deleteDepartment(
    municipalityId: Municipality["id"],
    departmentId: Department["id"]
  ) {
    return handle(() =>
      $departmentService.deleteDepartment(municipalityId, departmentId)
    );
  }

  function getProblemsInScope(
    municipalityId: Municipality["id"],
    departmentId: Department["id"],
    pagination: Pagination
  ) {
    return useAsyncData(
      `municipalities-${municipalityId}-departments-${departmentId}-problems`,
      () =>
        $departmentService.getProblemsInScope(
          municipalityId,
          departmentId,
          pagination
        )
    );
  }

  function getProblemInScope(
    municipalityId: Municipality["id"],
    departmentId: Department["id"],
    problemId: Problem["id"]
  ) {
    return useAsyncData(
      `municipalities-${municipalityId}-departments-${departmentId}-problems-${problemId}`,
      () =>
        $departmentService.getProblemInScope(
          municipalityId,
          departmentId,
          problemId
        )
    );
  }

  function changeProblemStatusInScope(
    municipalityId: Municipality["id"],
    departmentId: Department["id"],
    problemId: Problem["id"],
    request: ChangeProblemStatusInScopeRequest
  ) {
    return handle(() =>
      $departmentService.changeProblemStatusInScope(
        municipalityId,
        departmentId,
        problemId,
        request
      )
    );
  }

  return {
    loading,
    getDepartmentById,
    createDepartment,
    updateDepartment,
    deleteDepartment,
    getProblemsInScope,
    getProblemInScope,
    changeProblemStatusInScope,
  };
};
