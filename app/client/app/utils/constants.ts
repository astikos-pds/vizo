import type { LatLng } from "~/types/geolocation";

export const MAX_FILE_SIZE_IN_MB = 5;
export const MAX_FILE_SIZE_IN_BYTES = MAX_FILE_SIZE_IN_MB * 1024 * 1024;
export const MAX_ACCEPTABLE_ACCURACY_IN_METERS = 50;
export const MIN_AGE = 13;
export const MAX_RADIUS_IN_METERS = 1500;
export const CITY_CENTER: LatLng = { latitude: -23.5489, longitude: -46.6388 };
export const RADIUS_OF_RELATED_REPORTS_IN_METERS = 5.0;
export const REPORT_MAX_RADIUS_TO_UPDATE_COORDINATES_IN_METERS = 100;

export const MS_PER_DAY = 24 * 60 * 60 * 1000;
export const REPORT_CONFLICT_PERIOD_IN_DAYS = 14;
export const REPORT_CONFLICT_PERIOD_IN_MS =
  MS_PER_DAY * REPORT_CONFLICT_PERIOD_IN_DAYS;

export const PIN_INPUT_LENGTH = 6;

export const POINT_OF_INTEREST_MIN_RADIUS = 1;
export const POINT_OF_INTEREST_MAX_RADIUS = 3000;
