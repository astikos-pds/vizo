import { defineStore } from "pinia";

export const useOfficialStore = defineStore("official", () => {
  const email = ref("");

  function setEmail(newEmail: string) {
    email.value = newEmail;
  }

  return {
    email,
    setEmail,
  }
});
