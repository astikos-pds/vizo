export interface Problem {
  id: string;
  status: string;
  latitude: number;
  longitude: number;
  accumulatedCredibility: number;
  validated: boolean;
  firstReportedAt: string;
  lastReportedAt: string;
}
