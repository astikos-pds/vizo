import { defineStore } from "pinia";
import type { Official, UserProfile } from "~/types/domain";

export const useUserStore = defineStore("user", () => {
  const user = computed<UserProfile | null>({
    get: () => {
      const item = localStorage.getItem("user");
      if (!item) return null;
      return JSON.parse(item);
    },
    set: (value) => {
      localStorage.setItem("user", JSON.stringify(value));
    },
  });

  const setUser = (value: UserProfile | null) => {
    user.value = value;
  };

  const isAdmin = computed(() => {
    if (!user.value) return false;
    if (user.value.userType !== "OFFICIAL") return false;
    const profile = user.value.profile as Official;

    return profile.role === "ADMIN";
  });

  return {
    user,
    setUser,
    isAdmin,
  };
});
