import type { Map as LeafletMap, PointExpression } from "leaflet";
import type { LatLng } from "~/types/geolocation";

export const useMap = () => {
  const map = ref<LeafletMap | null>(null);
  const zoom = ref<number>(11);
  const center = reactive<LatLng>(CITY_CENTER);

  const { coords } = useMapGeolocation();

  const stopWatch = watch(
    coords,
    (newCoords) => {
      if (newCoords.latitude === Infinity || newCoords.longitude === Infinity)
        return;

      center.latitude = newCoords.latitude;
      center.longitude = newCoords.longitude;

      stopWatch();
    },
    { immediate: true }
  );

  return {
    map,
    zoom,
    center,
  };
};
