import type { GetUserWithFiltersParams } from "~/services/user";

export const useUsers = () => {
  const { $userService } = useNuxtApp();

  function getUserWithFilters(params: GetUserWithFiltersParams) {
    return useAsyncData(() => $userService.getUserWithFilters(params));
  }

  return {
    getUserWithFilters,
  };
};
