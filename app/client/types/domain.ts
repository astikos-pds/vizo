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

export interface Report {
  id: string;
  description: string;
  imagesUrls: string[];
  latitude: number;
  longitude: number;
  citizenId: string;
  problemId: string;
  createdAt: string;
}
