export const useApiHandler = () => {
  const loading = ref<boolean>(false);
  const error = ref<string>("");

  function getStatusError(error: Error): string {
    return error.message.split(" ").at(2) ?? "";
  }

  async function handle<T>(fn: () => Promise<T>): Promise<T | null> {
    loading.value = true;
    error.value = "";

    try {
      const response = await fn();
      return response;
    } catch (e: any) {
      if (e instanceof Error) {
        error.value = getStatusError(e);
      } else {
        console.error(e);
      }
      return null;
    } finally {
      loading.value = false;
    }
  }

  return { loading, error, handle };
};
