import { defineStore } from "pinia";

export const useAffiliationRequestStore = defineStore(
  "affiliation-request-store",
  () => {
    const institutionalEmail = ref<string | null>(null);
    const municipalityId = ref<string | null>(null);

    function setInstitutionalEmail(newEmail: string | null) {
      institutionalEmail.value = newEmail;
    }

    function setMunicipalityId(newId: string | null) {
      municipalityId.value = newId;
    }

    function reset() {
      setInstitutionalEmail(null);
      setMunicipalityId(null);
    }

    return {
      institutionalEmail,
      municipalityId,
      setInstitutionalEmail,
      setMunicipalityId,
      reset,
    };
  },
  {
    persist: true,
  }
);
