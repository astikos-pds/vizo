import { defineStore } from "pinia";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import type { AssignedUser } from "~/types/domain/assigned-user";
import type { User } from "~/types/domain/user";

export const useLoggedInUserStore = defineStore(
  "user-store",
  () => {
    const user = ref<User | null>(null);

    const setUser = (value: User | null) => {
      user.value = value;
    };

    const affiliations = ref<AffiliatedUser[]>([]);
    const currentAffiliationId = ref<string | null>(null);

    const currentAffiliation = computed(() => {
      if (!affiliations.value || !currentAffiliationId.value) return undefined;

      return affiliations.value.find(
        (a) => a.id === currentAffiliationId.value
      );
    });

    const assignments = ref<AssignedUser[]>([]);
    const currentAssignmentId = ref<string | null>(null);

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
  },
  {
    persist: true,
  }
);
