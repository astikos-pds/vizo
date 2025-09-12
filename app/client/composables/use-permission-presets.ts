import type {
  ExistsPermissionPresetInMunicipalityParams,
  MutatePermissionPresetRequest,
} from "~/services/permission-preset";
import type { Municipality } from "~/types/domain/municipality";
import type { PermissionPreset } from "~/types/domain/permission";

export const usePermissionPresets = () => {
  const { $permissionPresetService } = useNuxtApp();
  const { loading, handle } = useApiHandler();

  function getPermissionPresetsInMunicipality(
    municipalityId: Municipality["id"]
  ) {
    return useAsyncData(
      `municipalities-${municipalityId}-permission-presets`,
      () =>
        $permissionPresetService.getPermissionPresetsInMunicipality(
          municipalityId
        )
    );
  }

  function getPermissionPresetInMunicipality(
    municipalityId: Municipality["id"],
    permissionPresetId: PermissionPreset["id"]
  ) {
    return useAsyncData(
      `municipalities-${municipalityId}-permission-presets-${permissionPresetId}`,
      () =>
        $permissionPresetService.getPermissionPresetInMunicipality(
          municipalityId,
          permissionPresetId
        )
    );
  }

  function existsPermissionPresetByParamsInMunicipality(
    municipalityId: Municipality["id"],
    params: ExistsPermissionPresetInMunicipalityParams
  ) {
    return handle(() =>
      $permissionPresetService.existsPermissionPresetByParamsInMunicipality(
        municipalityId,
        params
      )
    );
  }

  function createPermissionPreset(
    municipalityId: Municipality["id"],
    request: MutatePermissionPresetRequest
  ) {
    return handle(() =>
      $permissionPresetService.createPermissionPreset(municipalityId, request)
    );
  }

  function updatePermissionPreset(
    municipalityId: Municipality["id"],
    permissionPresetId: PermissionPreset["id"],
    request: MutatePermissionPresetRequest
  ) {
    return handle(() =>
      $permissionPresetService.updatePermissionPreset(
        municipalityId,
        permissionPresetId,
        request
      )
    );
  }

  function deletePermissionPreset(
    municipalityId: Municipality["id"],
    permissionPresetId: PermissionPreset["id"]
  ) {
    return handle(() =>
      $permissionPresetService.deletePermissionPreset(
        municipalityId,
        permissionPresetId
      )
    );
  }

  return {
    loading,
    getPermissionPresetsInMunicipality,
    getPermissionPresetInMunicipality,
    existsPermissionPresetByParamsInMunicipality,
    createPermissionPreset,
    updatePermissionPreset,
    deletePermissionPreset,
  };
};
