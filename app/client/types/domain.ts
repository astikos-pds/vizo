export type ProblemType = string;

export interface Problem {
  id: string;
  status: string;
  latitude: number;
  longitude: number;
  type: ProblemType;
  accumulatedCredibility: number;
  validated: boolean;
  firstReportedAt: string;
  lastReportedAt: string;
}

export interface ReportImage {
  url: string;
}

export interface Report {
  id: string;
  description: string;
  images: ReportImage[];
  latitude: number;
  longitude: number;
  citizen: Citizen;
  problem: Problem;
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

export interface Department {
  id: string;
  municipality: Municipality;
  name: string;
  iconUrl: string;
  colorHex: string;
  problemTypes: ProblemType[];
  createdBy: Official;
  createdAt: string;
  updatedAt: string;
}

export type DepartmentRole = "COMMOM" | "COORDINATOR";

export interface Assignment {
  id: string;
  official: Official;
  department: Department;
  roleInDepartment: DepartmentRole;
  canUpdateStatus: boolean;
  canViewReports: boolean;
  canApproveOfficials: boolean;
  createdBy: Official;
  createdAt: string;
}

export type AffiliationRequestStatus = "PENDING" | "APPROVED" | "REJECTED";

export interface AffiliationRequest {
  id: string;
  official: Official;
  municipality: Municipality;
  status: AffiliationRequestStatus;
  createdAt: string;
  approvedBy: Official | null;
  approvedAt: string | null;
}

export interface Avatar {
  url: string;
}

export interface User {
  id: string;
  document: string;
  email: string;
  name: string;
  avatar: Avatar | null;
  createdAt: string;
  updatedAt: string;
}

export type Citizen = User & {
  credibilityPoints: number;
};

export type OfficialRole = "ADMIN" | "OFFICIAL";

export type Official = User & {
  role: OfficialRole;
  wasApproved: boolean;
};

export type UserType = "CITIZEN" | "OFFICIAL";
export type Profile = Citizen | Official;

export interface UserProfile {
  userType: UserType;
  profile: Profile;
}
