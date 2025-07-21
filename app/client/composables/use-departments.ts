import type { MunicipalityId } from "~/repositories/affiliation";
import {
  createDepartmentRepository,
  type CreateDepartmentRequest,
  type DepartmentId,
  type GetAllDepartmentsParams,
} from "~/repositories/department";

export const useDepartments = () => {
  const { $api } = useNuxtApp();
  const departmentRepository = createDepartmentRepository($api);

  function getDepartmentsByMunicipalityId(
    municipalityId: MunicipalityId,
    params: GetAllDepartmentsParams
  ) {
    return useAsyncData(`/municipalities/${municipalityId}/departments`, () =>
      departmentRepository.findAllByMunicipalityId(municipalityId, params)
    );
  }

  function createDepartment(
    municipalityId: MunicipalityId,
    body: CreateDepartmentRequest
  ) {
    return departmentRepository.create(municipalityId, body);
  }

  function deleteDepartment(
    municipalityId: MunicipalityId,
    departmentId: DepartmentId
  ) {
    return departmentRepository.delete(municipalityId, departmentId);
  }

  return {
    getDepartmentsByMunicipalityId,
    createDepartment,
    deleteDepartment,
  };
};
