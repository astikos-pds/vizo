import type { ExistsUserParams } from "~/services/user";

export const useUsers = () => {
  const { $userService } = useNuxtApp();
  const { loading, handle } = useApiHandler();

  function existsUserByParams(params: ExistsUserParams) {
    return handle(() => $userService.existsUserByParams(params));
  }

  return {
    loading,
    existsUserByParams,
  };
};
