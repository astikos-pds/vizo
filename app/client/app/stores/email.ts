import { defineStore } from "pinia";

export const useEmailStore = defineStore(
  "email",
  () => {
    const email = ref<string | null>(null);

    function setEmail(newEmail: string | null) {
      email.value = newEmail;
    }

    return {
      email,
      setEmail,
    };
  },
  {
    persist: true,
  }
);
