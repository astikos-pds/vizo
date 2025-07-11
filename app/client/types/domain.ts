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

export interface Municipality {
  id: string;
  name: string;
  emailDomain: string;
  iconUrl: string;
  createdAt: string;
  updatedAt: string;
}
