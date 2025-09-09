import type { ExistsUserParams } from "~/services/user";

export const useUsers = () => {
  const { $userService } = useNuxtApp();

  function existsUserByParams(params: ExistsUserParams) {
    return useAsyncData(() => $userService.existsUserByParams(params));
  }

  return {
    existsUserByParams,
  };
};
