import type { LatLng } from "~/types/geolocation";

export const MAX_FILE_SIZE_IN_MB = 5;
export const MAX_FILE_SIZE_IN_BYTES = MAX_FILE_SIZE_IN_MB * 1024 * 1024;
export const MAX_ACCEPTABLE_ACCURACY_IN_METERS = 50;
export const MIN_AGE = 13;
export const MAX_RADIUS_IN_METERS = 1500;
export const CITY_CENTER: LatLng = { latitude: -23.5489, longitude: -46.6388 };
