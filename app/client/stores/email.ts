import { defineStore } from "pinia";

export const useEmailStore = defineStore("email", () => {
  const email = useLocalStorage<string | null>("email", null);

  function setEmail(newEmail: string | null) {
    email.value = newEmail;
  }

  return {
    email,
    setEmail,
  };
});
