import { defineStore } from "pinia";
import type { User } from "~/types/domain/user";

export const useLoggedInUserStore = defineStore("user", () => {
  const user = useLocalStorage<User | null>("user", null);

  const setUser = (value: User | null) => {
    user.value = value;
  };

  return {
    user,
    setUser,
  };
});
