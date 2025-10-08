import type { LatLng } from "~/types/geolocation";
import {
  CITY_CENTER,
  MAX_ACCEPTABLE_ACCURACY_IN_METERS,
} from "~/utils/constants";

export const useMapGeolocation = () => {
  const isPermissionGranted = ref(false);

  const { coords, error, isSupported, resume } = useGeolocation({
    immediate: false,
  });
  const { $leafet } = useNuxtApp();

  const isPositionInacessible = computed(() => coords.value.accuracy === 0);

  const isLocationPrecise = computed(
    () =>
      coords.value &&
      coords.value.latitude !== Infinity &&
      coords.value.longitude !== Infinity &&
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

  function requestGeolocationPermission() {
    if (!isSupported.value) return;

    resume();
    isPermissionGranted.value = true;
  }

  onMounted(async () => {
    let geolocationPermission = false;
    if (isSupported && navigator.permissions) {
      const status = await navigator.permissions.query({ name: "geolocation" });
      geolocationPermission = status.state === "granted";
    }
    isPermissionGranted.value = geolocationPermission;
  });

  return {
    coords,
    error,
    isPositionInacessible,
    isLocationPrecise,
    mapCenter,
    markerPosition,
    isWithinRadius,
    updateMarker,
    isSupported,
    isPermissionGranted,
    requestGeolocationPermission,
  };
};
