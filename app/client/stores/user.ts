import { defineStore } from "pinia";
import type { Official, UserProfile } from "~/types/domain";

export const useUserStore = defineStore("user", () => {
  const user = useState<UserProfile>("user");

  const setUser = (value: UserProfile) => {
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
