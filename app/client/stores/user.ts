import { defineStore } from "pinia";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import type { User } from "~/types/domain/user";

export const useLoggedInUserStore = defineStore("user-store", () => {
  const user = useLocalStorage<User | null>("user", () => null, {
    serializer: {
      read: (v: string | null) => (v ? (JSON.parse(v) as User) : null),
      write: (v: User | null) => (v ? JSON.stringify(v) : ""),
    },
  });
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
    if (!affiliations || !currentAffiliationId) return undefined;

    return affiliations.value.find((a) => a.id === currentAffiliationId.value);
  });

  const setUser = (value: User | null) => {
    user.value = value;
  };

  const setAffiliations = (value: AffiliatedUser[]) => {
    affiliations.value = value ?? [];
  };

  const setCurrentAffiliation = (value: AffiliatedUser | null) => {
    currentAffiliationId.value = !value ? null : value.id;
  };

  const ensureUserIsAffiliatedTo = (municipalityId: string) => {
    if (!affiliations.value) return false;

    const affiliation = affiliations.value.find(
      (affiliation) => affiliation.municipality.id === municipalityId
    );

    return affiliation;
  };

  return {
    user,
    affiliations,
    currentAffiliation,
    setUser,
    setAffiliations,
    setCurrentAffiliation,
    ensureUserIsAffiliatedTo,
  };
});
