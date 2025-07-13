import { getProfileUseCase } from "~/services/users";

export const useProfile = () => {
  const { loading, handle } = useApiHandler();

  async function getProfile() {
    return await handle(() => getProfileUseCase());
  }

  return {
    loading,
    getProfile,
  };
};
