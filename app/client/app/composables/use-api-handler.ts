export const useApiHandler = () => {
  const loading = ref<boolean>(false);

  async function handle<T>(fn: () => Promise<T>): Promise<T | null> {
    loading.value = true;

    try {
      return await fn();
    } catch (e: any) {
      return null;
    } finally {
      loading.value = false;
    }
  }

  return { loading, handle };
};
