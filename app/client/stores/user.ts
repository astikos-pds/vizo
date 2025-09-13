import { defineStore } from "pinia";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import type { AssignedUser } from "~/types/domain/assigned-user";
import type { User } from "~/types/domain/user";

export const useLoggedInUserStore = defineStore("user-store", () => {
  const user = useLocalStorage<User | null>("user", () => null, {
    serializer: {
      read: (v: string | null) => (v ? (JSON.parse(v) as User) : null),
      write: (v: User | null) => (v ? JSON.stringify(v) : ""),
    },
  });

  const setUser = (value: User | null) => {
    user.value = value;
  };

  const affiliations = useLocalStorage<AffiliatedUser[]>(
    "affiliations",
    () => [],
    {
      serializer: {
        read: (v: string | null) =>
          v ? (JSON.parse(v) as AffiliatedUser[]) : [],
        write: (v: AffiliatedUser[] | null) =>
          v ? JSON.stringify(v) : JSON.stringify([]),
      },
    }
  );

  const currentAffiliationId = useLocalStorage<string | null>(
    "current-affiliation-id",
    () => null
  );

  const currentAffiliation = computed(() => {
    if (!affiliations.value || !currentAffiliationId.value) return undefined;

    return affiliations.value.find((a) => a.id === currentAffiliationId.value);
  });

  const setAffiliations = (value: AffiliatedUser[]) => {
    affiliations.value = value ?? [];
  };

  const setCurrentAffiliation = (value: AffiliatedUser | null) => {
    currentAffiliationId.value = !value ? null : value.id;
  };

  const ensureUserIsAffiliatedTo = (municipalityId: string) => {
    if (!affiliations.value) return undefined;

    const affiliation = affiliations.value.find(
      (affiliation) => affiliation.municipality.id === municipalityId
    );

    return affiliation;
  };

  const assignments = useLocalStorage<AssignedUser[]>("assignments", () => [], {
    serializer: {
      read: (v: string | null) => (v ? (JSON.parse(v) as AssignedUser[]) : []),
      write: (v: AssignedUser[] | null) =>
        v ? JSON.stringify(v) : JSON.stringify([]),
    },
  });

  const currentAssignmentId = useLocalStorage<string | null>(
    "current-assignment-id",
    () => null
  );

  const currentAssignment = computed(() => {
    if (
      !currentAffiliation.value ||
      !assignments.value ||
      !currentAssignmentId.value
    )
      return undefined;

    const assignment = assignments.value.find(
      (a) => a.id === currentAssignmentId.value
    );

    if (!assignment) return undefined;

    return {
      ...assignment,
      effectivePermission:
        assignment.permissionMode !== "CUSTOM" && assignment.permissionPreset
          ? assignment.permissionPreset.permission
          : assignment.customPermission,
    };
  });

  const setAssignments = (value: AssignedUser[]) => {
    assignments.value = value ?? [];
  };

  const setCurrentAssignment = (value: AssignedUser | null) => {
    currentAssignmentId.value = !value ? null : value.id;
  };

  const ensureUserIsAssignedTo = (departmentId: string) => {
    if (!assignments.value || !currentAffiliation.value) return undefined;

    const assignment = assignments.value.find(
      (assignment) => assignment.department.id === departmentId
    );

    return assignment;
  };

  return {
    user,
    setUser,
    affiliations,
    currentAffiliation,
    setAffiliations,
    setCurrentAffiliation,
    ensureUserIsAffiliatedTo,
    assignments,
    currentAssignment,
    setAssignments,
    setCurrentAssignment,
    ensureUserIsAssignedTo,
  };
});
