import type { LatLng } from "~/types/geolocation";
import {
  CITY_CENTER,
  MAX_ACCEPTABLE_ACCURACY_IN_METERS,
  MAX_RADIUS_IN_METERS,
} from "~/utils/constants";

export const useMapGeolocation = () => {
  const { coords, error } = useGeolocation();
  const { $leafet } = useNuxtApp();

  const isLocationPrecise = computed(
    () =>
      coords.value.accuracy <= MAX_ACCEPTABLE_ACCURACY_IN_METERS &&
      coords.value.accuracy > 0
  );

  const mapCenter = computed<LatLng>(() =>
    isLocationPrecise
      ? { latitude: coords.value.latitude, longitude: coords.value.longitude }
      : CITY_CENTER
  );

  const markerPosition = reactive<LatLng>({
    latitude: mapCenter.value.latitude,
    longitude: mapCenter.value.longitude,
  });

  function isWithinRadius(
    point1: LatLng,
    point2: LatLng,
    radius: number
  ): boolean {
    return (
      $leafet
        .latLng(point1.latitude, point1.longitude)
        .distanceTo($leafet.latLng(point2.latitude, point2.longitude)) <= radius
    );
  }

  function updateMarker(newLatLng: LatLng) {
    markerPosition.latitude = newLatLng.latitude;
    markerPosition.longitude = newLatLng.longitude;
  }

  return {
    coords,
    error,
    isLocationPrecise,
    mapCenter,
    markerPosition,
    isWithinRadius,
    updateMarker,
  };
};
