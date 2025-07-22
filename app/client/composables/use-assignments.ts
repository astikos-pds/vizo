import {
  createAssignmentRepository,
  type CreateAssignmentsInBatchResquest,
  type GetAssignmentsParams,
} from "~/repositories/assignment";
import type { DepartmentId } from "~/repositories/department";
import type { MunicipalityId } from "~/repositories/municipality";

export const useAssignments = () => {
  const { $api } = useNuxtApp();
  const assignmentRepository = createAssignmentRepository($api);

  function getAssignmentsByDepartmentId(
    municipalityId: MunicipalityId,
    departmentId: DepartmentId,
    params?: GetAssignmentsParams
  ) {
    return useAsyncData(
      `municipalities-${municipalityId}-departments-${departmentId}-assignments`,
      () =>
        assignmentRepository.findAllByDepartmentId(
          municipalityId,
          departmentId,
          params
        )
    );
  }

  function assignToDepartmentInBatch(
    municipalityId: MunicipalityId,
    departmentId: DepartmentId,
    body: CreateAssignmentsInBatchResquest
  ) {
    return assignmentRepository.createInBatch(
      municipalityId,
      departmentId,
      body
    );
  }

  return {
    getAssignmentsByDepartmentId,
    assignToDepartmentInBatch,
  };
};
