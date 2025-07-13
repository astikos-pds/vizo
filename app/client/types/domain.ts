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

export type AffiliationRequestStatus = "PENDING" | "APPROVED" | "REJECTED";

export interface AffiliationRequest {
  id: string;
  officialId: string;
  municipalityId: string;
  status: AffiliationRequestStatus;
  createdAt: string;
  approvedById: string | null;
  approvedAt: string | null;
}

export interface Citizen {
  id: string;
  email: string;
  name: string;
  credibilityPoints: number;
  createdAt: string;
  updatedAt: string;
}

export type OfficialRole = "ADMIN" | "OFFICIAL";

export interface Official {
  id: string;
  document: string;
  email: string;
  name: string;
  role: OfficialRole;
  wasApproved: boolean;
  createdAt: string;
  updatedAt: string;
}

export type UserType = "CITIZEN" | "OFFICIAL";
export type Profile = Citizen | Official;

export interface UserProfile {
  userType: UserType;
  profile: Profile;
}
