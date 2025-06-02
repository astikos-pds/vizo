export const useApiHandler = () => {
  const loading = ref<boolean>(false);
  const error = ref<string>("");

  function getError(status?: number | string): string {
    if (status == 400) return "Bad request";
    if (status == 401) return "Invalid credentials";
    if (status == 403) return "Forbidden";
    if (status == 404) return "Not found";
    if (status == 500) return "Internal server error";
    return "An unexpected error occurred.";
  }

  async function handle<T>(fn: () => Promise<T>): Promise<T | null> {
    loading.value = true;
    error.value = "";

    try {
      const response = await fn();
      return response;
    } catch (e: any) {
      if (e instanceof Error) {
        console.error(e.message);
        error.value = getError(e.message.split(" ").at(2) ?? "");
      }
      return null;
    } finally {
      loading.value = false;
    }
  }

  return { loading, error, handle };
};
