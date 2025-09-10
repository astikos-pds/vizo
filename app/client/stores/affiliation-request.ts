import { defineStore } from "pinia";

export const useAffiliationRequestStore = defineStore(
  "affiliation-request-store",
  () => {
    const institutionalEmail = useLocalStorage<string | null>(
      "institutional_email",
      null
    );

    const municipalityId = useLocalStorage<string | null>(
      "municipality_id",
      null
    );

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
  }
);
