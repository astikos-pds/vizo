import { defineStore } from "pinia";

export const useEmailStore = defineStore("email", () => {
  const email = ref("");

  function setEmail(newEmail: string) {
    email.value = newEmail;
  }

  return {
    email,
    setEmail,
  };
});
