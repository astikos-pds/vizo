import { getProfileUseCase } from "~/services/users";

export const useUser = () => {
  const { loading, handle } = useApiHandler();

  async function getProfile() {
    return await handle(() => getProfileUseCase());
  }

  return {
    loading,
    getProfile,
  };
};
