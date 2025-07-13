import { defineStore } from "pinia";
import type { UserProfile } from "~/types/domain";

export const useUserStore = defineStore("user", () => {
  const user = ref<UserProfile>();

  const setUser = (value: UserProfile) => {
    user.value = value;
  };

  return {
    user,
    setUser,
  };
});
