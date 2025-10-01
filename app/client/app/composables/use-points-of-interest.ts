import type { MutatePointOfInterestRequest } from "~/services/point-of-interest";
import type { PointOfInterest } from "~/types/domain/point-of-interest";

export const usePointsOfInterest = () => {
  const { $pointOfInterestService } = useNuxtApp();
  const { handle, loading } = useApiHandler();

  function getPointOfInterest(id: PointOfInterest["id"]) {
    return useAsyncData(`points-of-interest-${id}`, () =>
      $pointOfInterestService.getPointOfInterestById(id)
    );
  }

  function createPointOfInterest(request: MutatePointOfInterestRequest) {
    return handle(() => $pointOfInterestService.createPointOfInterest(request));
  }

  function updatePointOfInterest(
    id: PointOfInterest["id"],
    request: MutatePointOfInterestRequest
  ) {
    return handle(() =>
      $pointOfInterestService.updatePointOfInterest(id, request)
    );
  }

  function deletePointOfInterest(id: PointOfInterest["id"]) {
    return handle(() => $pointOfInterestService.deletePointOfInterest(id));
  }

  return {
    loading,
    getPointOfInterest,
    createPointOfInterest,
    updatePointOfInterest,
    deletePointOfInterest,
  };
};
