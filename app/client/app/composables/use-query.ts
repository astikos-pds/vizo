import type { UseFetchOptions } from "nuxt/app";

export const useQuery = <T = any>(
  url: string | (() => string) | Ref<string>,
  options: UseFetchOptions<T> = {}
) => {
  const nuxtApp = useNuxtApp();

  return useFetch(url, {
    ...options,
    $fetch: nuxtApp.$api,
  });
};
