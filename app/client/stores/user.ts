import { defineStore } from "pinia";
import type { UserProfile } from "~/types/domain";

export const useUserStore = defineStore("user", () => {
  const user = useState<UserProfile>("user");

  const setUser = (value: UserProfile) => {
    user.value = value;
  };

  return {
    user,
    setUser,
  };
});
